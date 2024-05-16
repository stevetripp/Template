package com.example.template.ux.video

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.text.CueGroup

interface SharedCastManagerListeners {

    // AudioEvents
    fun onAudioCastSessionAvailable() {}
    fun onAudioCastSessionUnavailable() {}
    fun onAudioCastEvents(player: Player, events: Player.Events) {}
    fun onAudioCastPlaybackStateChanged(playbackState: Int) {}
    fun onAudioCastSkippedToNewTrack(newTrack: MediaItem) {}

    // Video Events
    fun onVideoCastSessionAvailable() {}
    fun onVideoCastSessionUnavailable() {}
    fun onVideoCastPlaybackStateChanged(playbackState: Int) {}
    fun onVideoCastEvents(player: Player, events: Player.Events) {}
    fun onVideoCastCues(cueGroup: CueGroup) {}
    fun onVideoCastSkippedToNewTrack(newTrack: MediaItem) {}

}
