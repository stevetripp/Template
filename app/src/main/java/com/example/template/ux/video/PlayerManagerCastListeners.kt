package com.example.template.ux.video

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.text.CueGroup

interface PlayerManagerCastListeners : SharedCastManagerListeners {
    override fun onVideoCastSessionAvailable()
    override fun onVideoCastSessionUnavailable()
    override fun onVideoCastPlaybackStateChanged(playbackState: Int)
    override fun onVideoCastEvents(player: Player, events: Player.Events)
    override fun onVideoCastCues(cueGroup: CueGroup)
    override fun onVideoCastSkippedToNewTrack(newTrack: MediaItem)

}
