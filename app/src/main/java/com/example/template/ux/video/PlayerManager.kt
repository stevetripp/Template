package com.example.template.ux.video

import android.media.session.PlaybackState
import androidx.core.content.ContextCompat
import androidx.media3.cast.CastPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.text.CueGroup
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import com.example.template.R
import com.example.template.ux.video.MediaPlayerItem.Companion.IMAGE_RENDITIONS_BUNDLE_EXTRA_KEY
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class PlayerManager(
    private val activity: VideoActivity,
    private val playerView: PlayerView,
    private val downloadCache: Cache,
    private val playbackPosition: (Long?, Boolean, VideoId?) -> Long,
    private val isPlayingChanged: (Boolean) -> Unit,
    private val viewModel: VideoViewModel
) : Player.Listener, PlayerManagerCastListeners {
    private var isClosedCaptionsOnEventSent = false
    private val castPlayerManager: CastPlayerManager
        get() = viewModel.castPlayerManager
    val currentLanguage: Iso3Locale
        get() = activity.viewModel.currentLanguage.value

    private val context = activity.applicationContext
    private var mediaSession: MediaSession? = null
    private val localPlayer: ExoPlayer = ExoPlayer.Builder(context)
        .setSeekBackIncrementMs(DEFAULT_SEEK_INCREMENT)
        .setSeekForwardIncrementMs(DEFAULT_SEEK_INCREMENT)
        .build()
        .apply {
            trackSelectionParameters = trackSelectionParameters.buildUpon().setMaxVideoSizeSd().setPreferredAudioLanguage(currentLanguage.value).build()
            addListener(this@PlayerManager)
            mediaSession = MediaSession.Builder(context, this).setId(SESSION_ID).build()
        }
    private val castPlayer: CastPlayer?
        get() = castPlayerManager.castPlayer

    var currentPlayer: Player? = null

    private var currentMediaPlayerItemId: String? = null
    private var currentMediaPlayerPlayList: List<MediaPlayerItem>? = null

    val isPlaying: Boolean
        get() = currentPlayer?.isPlaying ?: false

    val isCasting: Boolean
        get() = currentPlayer == castPlayer && currentPlayer?.isPlaying == true

    private val isCastSessionAvailable: Boolean
        get() = castPlayer?.isCastSessionAvailable == true

    init {
        castPlayerManager.setCastOwnerAndListeners(CastOwner.PlayerManager, this@PlayerManager)
        if (isCastSessionAvailable) {
            if (castPlayerManager.lastCastSessionOpenedBy == CastOwner.PlayerManager) {
                castPlayer?.let { setCurrentPlayer(it, true) }
            } else {
                castPlayerManager.tearDownCastPlayer()
                setCurrentPlayer(localPlayer)
            }
        } else {
            setCurrentPlayer(localPlayer)
        }
    }

    fun skipToNextTrack() {
        currentPlayer?.seekToNext()
    }

    override fun onVideoCastSkippedToNewTrack(newTrack: MediaItem) {
        currentMediaPlayerItemId = newTrack.mediaId
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) = isPlayingChanged(isPlaying)

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        onPlayerManagerEvents(player, events)
    }

    override fun onVideoCastEvents(player: Player, events: Player.Events) {
        onPlayerManagerEvents(player, events)
    }

    private fun onPlayerManagerEvents(player: Player, events: Player.Events) {
        if (events.contains(Player.EVENT_TRACK_SELECTION_PARAMETERS_CHANGED)) {
            val trackSelectionParameters = player.trackSelectionParameters.preferredAudioLanguages
//            (activity).logTrackSelectionParameters(trackSelectionParameters.first())
        }
    }

    override fun onCues(cueGroup: CueGroup) {
        super.onCues(cueGroup)
        onPlayerManagerCues()
    }

    override fun onVideoCastCues(cueGroup: CueGroup) {
        onPlayerManagerCues()
    }

    private fun onPlayerManagerCues() {
        if (!isClosedCaptionsOnEventSent) {
            isClosedCaptionsOnEventSent = true
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        onPlayerMangerPlaybackStateChanged(playbackState)
    }

    override fun onVideoCastPlaybackStateChanged(playbackState: Int) {
        onPlayerMangerPlaybackStateChanged(playbackState)
    }

    private fun onPlayerMangerPlaybackStateChanged(playbackState: Int) {
        if (playbackState == PlaybackState.STATE_FAST_FORWARDING) {
            release()
            activity.finish()
        }
    }

    override fun onVideoCastSessionAvailable() {
        setCurrentPlayer(castPlayer)
    }

    override fun onVideoCastSessionUnavailable() {
        setCurrentPlayer(localPlayer)
    }

    private fun setCurrentPlayer(newPlayer: Player?, grabCurrentCastSession: Boolean = false) {
        if (currentPlayer == newPlayer) return

        newPlayer?.let { setupPlayerView(it) }
        if (!grabCurrentCastSession) tearDownPreviousPlayer(currentPlayer)

        currentPlayer = newPlayer
        currentMediaPlayerItemId?.let { playerItemId ->
            if (!grabCurrentCastSession) {
                currentMediaPlayerPlayList?.let { playList ->
                    playMediaItem(VideoId(playerItemId), playList.mapNotNull { it.toMediaItem() })
                }
            }
        }
    }

    private fun setupPlayerView(newPlayer: Player) {
        playerView.player = newPlayer
        playerView.controllerHideOnTouch = newPlayer === localPlayer

        if (newPlayer === castPlayer) {
            playerView.controllerShowTimeoutMs = 0
            playerView.showController()
            playerView.defaultArtwork = ContextCompat.getDrawable(context, R.drawable.quantum_ic_cast_connected_white_24)
        } else {
            playerView.controllerShowTimeoutMs = PlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
            playerView.defaultArtwork = null
        }
    }

    private fun tearDownPreviousPlayer(previousPlayer: Player?) {
        previousPlayer?.let { player ->
            if (player.playbackState != Player.STATE_ENDED) {
                playbackPosition(player.currentPosition, false, null)
            }
            if (player == castPlayer) {
                castPlayerManager.tearDownCastPlayer()
            } else {
                player.stop()
                player.clearMediaItems()
            }
        }
    }

    fun setMediaItems(mediaPlayerItemPlayList: List<MediaPlayerItem>, videoId: VideoId) {
        val toPlayItem = mediaPlayerItemPlayList.find { it.id.value == videoId.value }?.toMediaItem()
        if (toPlayItem?.mediaId == currentMediaPlayerItemId) return

        playbackPosition(0, false, null)
        currentMediaPlayerItemId = toPlayItem?.mediaId
        currentMediaPlayerPlayList = mediaPlayerItemPlayList
        if (isCastSessionAvailable && castPlayerManager.lastCastSessionOpenedBy == CastOwner.PlayerManager && castPlayerManager.currentCastedItemId == toPlayItem?.mediaId) return
        playMediaItem(videoId, mediaPlayerItemPlayList.mapNotNull { it.toMediaItem() })
    }

    private fun playMediaItem(videoId: VideoId, mediaItemPlayList: List<MediaItem>) {
        if (currentPlayer == localPlayer) {
            playOnLocalPlayer(videoId, mediaItemPlayList)
        } else {
            playOnCastPlayer(videoId, mediaItemPlayList)
        }
    }

    private fun playOnLocalPlayer(videoId: VideoId, mediaItemPlayList: List<MediaItem>) {
        val toPlayItem = mediaItemPlayList.find { it.mediaId == videoId.value }
        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaSourceFactories = mediaItemPlayList.map {
            when (it.localConfiguration?.mimeType) {
                MimeTypeUtil.MediaType.DASH.mimeType -> DashMediaSource.Factory(dataSourceFactory)
                MimeTypeUtil.MediaType.HLS.mimeType -> HlsMediaSource.Factory(dataSourceFactory)
                else -> {
                    // Add support for downloaded content
                    val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                    val cacheDataSourceFactory: DataSource.Factory = CacheDataSource.Factory()
                        .setCache(downloadCache)
                        .setUpstreamDataSourceFactory(httpDataSourceFactory)
                        .setCacheWriteDataSinkFactory(null) // Disable writing.

                    DefaultMediaSourceFactory(cacheDataSourceFactory)
                }
            }.createMediaSource(it)
        }
        localPlayer.setMediaSources(mediaSourceFactories)
        val startingIndex = mediaItemPlayList.indexOf(toPlayItem)
        currentPlayer?.apply {
            seekTo(startingIndex, playbackPosition(null, false, null))
            playWhenReady = true
            prepare()
        }
    }

    fun getSecondsToNextTrackFlow(pollInterval: Long = DEFAULT_POLL_INTERVAL_MS): Flow<NextTrack?> {
        return flow {
            emit(getProgressState())
            while (true) {
                delay(pollInterval)
                emit(getProgressState())
            }
        }
    }

    private fun getProgressState(): NextTrack? {
        val currentMediaItemId = currentPlayer?.currentMediaItem?.mediaId
        playbackPosition(currentPlayer?.currentPosition ?: 0L, true, currentMediaItemId?.let { VideoId(it) })
        return currentPlayer?.let { player ->
            if (player.hasNextMediaItem() && player.duration > 0) {
                val nextMediaItem = currentPlayer?.getMediaItemAt(player.nextMediaItemIndex)
                nextMediaItem?.let { next ->
                    NextTrack(
                        secondsUntil = (player.duration.div(1000) - player.currentPosition.div(1000)).toInt(),
                        nextTrackTitle = next.mediaMetadata.title.toString(),
                        nextTrackImageRenditions = next.mediaMetadata.extras?.getString(IMAGE_RENDITIONS_BUNDLE_EXTRA_KEY).orEmpty()
                    )
                }
            } else null
        }
    }

    private fun playOnCastPlayer(videoId: VideoId, mediaItemPlayList: List<MediaItem>) {
        castPlayerManager.playOnCast(videoId, mediaItemPlayList, playbackPosition(null, false, null), CastOwner.PlayerManager)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.mediaId?.let {
            currentMediaPlayerItemId = it
            currentPlayer?.seekTo(viewModel.getVideoInitialPosition(VideoId(it)))
        }
    }

    fun release() {
        val currentMediaItemId = currentPlayer?.currentMediaItem?.mediaId
        playbackPosition(currentPlayer?.currentPosition ?: 0L, true, currentMediaItemId?.let { VideoId(it) })
        playerView.player = null
        localPlayer.release()
        mediaSession?.apply {
            release()
            mediaSession = null
        }
    }

    companion object {
        const val INITIAL_PLAYBACK_POSITION = 0L
        private const val DEFAULT_SEEK_INCREMENT = 10000L
        private const val DEFAULT_POLL_INTERVAL_MS: Long = 1000
        private const val SESSION_ID = "gs_video_session"
    }
}