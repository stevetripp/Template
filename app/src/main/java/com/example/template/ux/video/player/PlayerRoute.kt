package com.example.template.ux.video.player

import androidx.navigation3.runtime.NavKey
import com.example.template.domain.VideoId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(
    @SerialName(PlayerRouteArgs.VIDEO_ID)
    val videoId: VideoId
) : NavKey

object PlayerRouteArgs {
    const val VIDEO_ID = "videoId"
}
