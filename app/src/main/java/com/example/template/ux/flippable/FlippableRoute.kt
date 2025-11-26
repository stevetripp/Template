package com.example.template.ux.flippable

import androidx.navigation.navDeepLink
import androidx.navigation3.runtime.NavKey
import com.example.template.ux.DeepLink
import com.example.template.ux.main.Screen
import kotlinx.serialization.Serializable

@Serializable
object FlippableRoute : NavKey

fun FlippableRoute.deepLinks() = listOf(
    navDeepLink<FlippableRoute>(basePath = "${DeepLink.ROOT}/${Screen.FLIPPABLE.name}")
)