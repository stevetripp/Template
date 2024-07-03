package com.example.template.ux.video.player

import com.example.template.ux.NavTypeMaps
import com.example.template.ux.video.VideoId
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute
import kotlin.reflect.typeOf

@Serializable
data class PlayerRoute(val videoId: VideoId) : NavigationRoute

fun PlayerRoute.Companion.typeMap() = mapOf(
    typeOf<VideoId>() to NavTypeMaps.VideoIdNavType
)