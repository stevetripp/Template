package com.example.template.ux.video

import androidx.media3.cast.CastPlayer
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.text.CueGroup
import com.google.android.gms.cast.framework.CastContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.lds.media.cast.CastManager
import org.lds.media.cast.ChurchCastMediaItemConverter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CastPlayerManager @Inject constructor(
    castManager: CastManager
) : Player.Listener, SessionAvailabilityListener {

    private val castContext: CastContext? = castManager.castContext

    private val castOwner: MutableStateFlow<CastOwner?> = MutableStateFlow(null)

    private val listeners: MutableStateFlow<SharedCastManagerListeners?> = MutableStateFlow(null)

    private val mutableCastPlayer: MutableStateFlow<CastPlayer?> = MutableStateFlow(getNewCastPlayer())

    val castPlayer: CastPlayer?
        get() = mutableCastPlayer.value

    val currentCastedItemId: String?
        get() = if (castPlayer?.isCastSessionAvailable == true) castPlayer?.currentMediaItem?.mediaId else null

    val lastCastSessionOpenedBy: CastOwner?
        get() = if (castPlayer?.isCastSessionAvailable == true) castPlayer?.mediaMetadata?.extras?.getInt("cast_session_owner")?.let { CastOwner.entries[it] } else null

    fun playOnCast(
        videoId: VideoId,
        mediaItemPlayList: List<MediaItem>,
        progress: Long?,
        owner: CastOwner
    ) {
        castOwner.value = owner
        val startingIndex = mediaItemPlayList.indexOf(mediaItemPlayList.find { it.mediaId == videoId.value })
        val ownedMediaItems = mediaItemPlayList.map {
            val extras = it.mediaMetadata.extras?.apply {
                putInt("cast_session_owner", owner.ordinal)
            }
            val mediaMetadata = it.mediaMetadata.buildUpon().setExtras(extras).build()
            it.buildUpon().setMediaMetadata(mediaMetadata).build()
        }

        castPlayer?.apply {
            if (progress !== null) {
                setMediaItems(ownedMediaItems, startingIndex, progress)
            } else {
                setMediaItems(ownedMediaItems)
            }
            playWhenReady = true
            prepare()
            play()
        }
    }

    private fun getNewCastPlayer(): CastPlayer? = castContext?.let {
        CastPlayer(it, ChurchCastMediaItemConverter(), DEFAULT_SEEK_INCREMENT, DEFAULT_SEEK_INCREMENT).apply {
            addListener(this@CastPlayerManager)
            setSessionAvailabilityListener(this@CastPlayerManager)
        }
    }

    fun tearDownCastPlayer() {
        castPlayer?.stop()
        castPlayer?.clearMediaItems()
        castPlayer?.release()
        mutableCastPlayer.update { getNewCastPlayer() }
    }

    fun setCastOwnerAndListeners(newCastOwner: CastOwner, newListeners: SharedCastManagerListeners) {
        castOwner.value = newCastOwner
        listeners.value = newListeners
    }

    override fun onCastSessionAvailable() {
        when (castOwner.value) {
            CastOwner.MediaBrowserManager -> {
                listeners.value?.onAudioCastSessionAvailable()
            }

            CastOwner.PlayerManager -> {
                listeners.value?.onVideoCastSessionAvailable()
            }

            else -> {}
        }
    }

    override fun onCastSessionUnavailable() {
        when (castOwner.value) {
            CastOwner.MediaBrowserManager -> {
                listeners.value?.onAudioCastSessionUnavailable()
            }

            CastOwner.PlayerManager -> {
                listeners.value?.onVideoCastSessionUnavailable()
            }

            else -> {}
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        when (castOwner.value) {
            CastOwner.MediaBrowserManager -> {
                listeners.value?.onAudioCastEvents(player, events)
            }

            CastOwner.PlayerManager -> {
                listeners.value?.onVideoCastEvents(player, events)
            }

            else -> {}
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (castOwner.value) {
            CastOwner.MediaBrowserManager -> {
                listeners.value?.onAudioCastPlaybackStateChanged(playbackState)
            }

            CastOwner.PlayerManager -> {
                listeners.value?.onVideoCastPlaybackStateChanged(playbackState)
            }

            else -> {}
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            when (castOwner.value) {
                CastOwner.MediaBrowserManager -> listeners.value?.onAudioCastSkippedToNewTrack(it)
                CastOwner.PlayerManager -> listeners.value?.onVideoCastSkippedToNewTrack(it)
                else -> {}
            }
        }
    }

    override fun onCues(cueGroup: CueGroup) {
        super.onCues(cueGroup)
        listeners.value?.onVideoCastCues(cueGroup)
    }

    companion object {
        private const val DEFAULT_SEEK_INCREMENT = 10000L
    }

}
