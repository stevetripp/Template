package com.example.template.ux.video.player

import com.example.template.ux.video.VideoId
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute

@Serializable
data class PlayerRoute(val videoId: VideoId) : NavigationRoute
