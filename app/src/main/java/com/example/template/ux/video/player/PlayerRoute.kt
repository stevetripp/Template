package com.example.template.ux.video.player

import androidx.navigation3.runtime.NavKey
import com.example.template.ux.video.VideoId
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(val videoId: VideoId) : NavKey
