/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.template.ux.video.player

import android.content.Context
import android.view.KeyEvent
import androidx.core.content.res.ResourcesCompat
import androidx.media3.cast.CastPlayer
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.DiscontinuityReason
import androidx.media3.common.Player.TimelineChangeReason
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import com.example.template.R
import com.example.template.util.SmtLogger
import com.google.android.gms.cast.framework.CastContext

/**
 * Copied from https://github.com/androidx/media/blob/release/demos/cast/src/main/java/androidx/media3/demo/cast/PlayerManager.java
 *
 * Creates a new manager for [ExoPlayer] and [CastPlayer].
 *
 * @param context A [Context].
 * @param listener A [Listener] for queue position changes.
 * @param playerView The [PlayerView] for playback.
 * @param castContext The [CastContext].
 */
@Suppress("UnsafeCallOnNullableType")
internal class PlayerManager(
    private val context: Context,
    private val listener: Listener,
    private val playerView: PlayerView,
    castContext: CastContext?
) : Player.Listener, SessionAvailabilityListener {
    /** Listener for events.  */
    internal interface Listener {
        /** Called when the currently played item of the media queue changes.  */
        fun onQueuePositionChanged(previousIndex: Int, newIndex: Int)

        /**
         * Called when a track of type `trackType` is not supported by the player.
         *
         * @param trackType One of the [C]`.TRACK_TYPE_*` constants.
         */
        fun onUnsupportedTrack(trackType: Int)
    }

    private val localPlayer: Player
    private val castPlayer: CastPlayer
    private val mediaQueue = ArrayList<MediaItem>()

    private var lastSeenTracks: Tracks? = null
    /** Returns the index of the currently played item.  */
    private var currentItemIndex: Int
    private var currentPlayer: Player? = null

    init {
        SmtLogger.i("""init""")
        currentItemIndex = C.INDEX_UNSET

        localPlayer = ExoPlayer.Builder(context).build()
        localPlayer.addListener(this)

        castPlayer = CastPlayer(castContext!!)
        castPlayer.addListener(this)
        castPlayer.setSessionAvailabilityListener(this)

        setCurrentPlayer(if (castPlayer.isCastSessionAvailable) castPlayer else localPlayer)
    }

    fun isPlaying(): Boolean = currentPlayer?.isPlaying == true
    fun isCasting(): Boolean = currentPlayer == castPlayer && isPlaying()

    // Queue manipulation methods.

    /**
     * Appends `item` to the media queue.
     *
     * @param item The [MediaItem] to append.
     */
    fun addItem(item: MediaItem) {
        mediaQueue.add(item)
        currentPlayer!!.addMediaItem(item)
    }

    /**
     * Dispatches a given [KeyEvent] to the corresponding view of the current player.
     *
     * @param event The [KeyEvent].
     * @return Whether the event was handled by the target view.
     */
    fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return playerView.dispatchKeyEvent(event!!)
    }

    /** Releases the manager and the players that it holds.  */
    fun release() {
        currentItemIndex = C.INDEX_UNSET
        mediaQueue.clear()
        playerView.player = null
        localPlayer.release()
    }

    // Player.Listener implementation.
    override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
        updateCurrentItemIndex()
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: @DiscontinuityReason Int
    ) {
        updateCurrentItemIndex()
    }

    override fun onTimelineChanged(timeline: Timeline, reason: @TimelineChangeReason Int) {
        updateCurrentItemIndex()
    }

    override fun onTracksChanged(tracks: Tracks) {
        if (currentPlayer !== localPlayer || tracks === lastSeenTracks) {
            return
        }
        if (tracks.containsType(C.TRACK_TYPE_VIDEO)
            && !tracks.isTypeSupported(C.TRACK_TYPE_VIDEO,  /* allowExceedsCapabilities= */true)
        ) {
            listener.onUnsupportedTrack(C.TRACK_TYPE_VIDEO)
        }
        if (tracks.containsType(C.TRACK_TYPE_AUDIO)
            && !tracks.isTypeSupported(C.TRACK_TYPE_AUDIO,  /* allowExceedsCapabilities= */true)
        ) {
            listener.onUnsupportedTrack(C.TRACK_TYPE_AUDIO)
        }
        lastSeenTracks = tracks
    }

    // CastPlayer.SessionAvailabilityListener implementation.
    override fun onCastSessionAvailable() {
        SmtLogger.i("""onCastSessionAvailable()""")
        setCurrentPlayer(castPlayer)
    }

    override fun onCastSessionUnavailable() {
        SmtLogger.i("""onCastSessionUnavailable()""")
        setCurrentPlayer(localPlayer)
    }

    // Internal methods.
    private fun updateCurrentItemIndex() {
        val playbackState = currentPlayer!!.playbackState
        maybeSetCurrentItemAndNotify(
            if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED
            ) currentPlayer!!.currentMediaItemIndex
            else C.INDEX_UNSET
        )
    }

    private fun setCurrentPlayer(currentPlayer: Player) {
        if (this.currentPlayer === currentPlayer) {
            return
        }

        SmtLogger.i("""setCurrentPlayer($currentPlayer)""")
        playerView.player = currentPlayer
        playerView.controllerHideOnTouch = currentPlayer === localPlayer
        if (currentPlayer === castPlayer) {
            playerView.controllerShowTimeoutMs = 0
            playerView.showController()
            playerView.defaultArtwork = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_baseline_cast_connected_400,  /* theme= */
                null
            )
        } else { // currentPlayer == localPlayer
            playerView.controllerShowTimeoutMs = PlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
            playerView.defaultArtwork = null
        }

        // Player state management.
        var playbackPositionMs = C.TIME_UNSET
        var currentItemIndex = C.INDEX_UNSET
        var playWhenReady = false

        val previousPlayer = this.currentPlayer
        if (previousPlayer != null) {
            // Save state from the previous player.
            val playbackState = previousPlayer.playbackState
            if (playbackState != Player.STATE_ENDED) {
                playbackPositionMs = previousPlayer.currentPosition
                playWhenReady = previousPlayer.playWhenReady
                currentItemIndex = previousPlayer.currentMediaItemIndex
                if (currentItemIndex != this.currentItemIndex) {
                    playbackPositionMs = C.TIME_UNSET
                    currentItemIndex = this.currentItemIndex
                }
            }
            previousPlayer.stop()
            previousPlayer.clearMediaItems()
        }

        this.currentPlayer = currentPlayer

        SmtLogger.i(
            """currentItemIndex: $currentItemIndex
            |playbackPositionMs: $playbackPositionMs
        """.trimMargin()
        )
        // Media queue management.
        currentPlayer.setMediaItems(mediaQueue, currentItemIndex, playbackPositionMs)
        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.prepare()
    }

    /**
     * Starts playback of the item at the given index.
     *
     * @param itemIndex The index of the item to play.
     */
    fun setCurrentItem(itemIndex: Int) {
        SmtLogger.i("""setCurrentItem($itemIndex)""")
        maybeSetCurrentItemAndNotify(itemIndex)
        if (currentPlayer!!.currentTimeline.windowCount != mediaQueue.size) {
            // This only happens with the cast player. The receiver app in the cast device clears the
            // timeline when the last item of the timeline has been played to end.
            currentPlayer!!.setMediaItems(mediaQueue, itemIndex, C.TIME_UNSET)
        } else {
            currentPlayer!!.seekTo(itemIndex, C.TIME_UNSET)
        }
        currentPlayer!!.playWhenReady = true
    }

    private fun maybeSetCurrentItemAndNotify(currentItemIndex: Int) {
        if (this.currentItemIndex != currentItemIndex) {
            val oldIndex = this.currentItemIndex
            this.currentItemIndex = currentItemIndex
            listener.onQueuePositionChanged(oldIndex, currentItemIndex)
        }
    }
}
