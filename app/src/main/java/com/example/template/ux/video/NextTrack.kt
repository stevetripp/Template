package com.example.template.ux.video

class NextTrack(
    val secondsUntil: Int,
    val nextTrackImageRenditions: String,
    val nextTrackTitle: String
) {
    val stringSecondsUntil = secondsUntil.toString()
}
