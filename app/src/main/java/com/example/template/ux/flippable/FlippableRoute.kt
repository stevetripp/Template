package com.example.template.ux.flippable

import androidx.navigation.navDeepLink
import com.example.template.ux.DeepLink
import com.example.template.ux.main.Screen
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute

@Serializable
object FlippableRoute : NavigationRoute

fun FlippableRoute.deepLinks() = listOf(
    navDeepLink<FlippableRoute>(basePath = "${DeepLink.ROOT}/${Screen.FLIPPABLE.name}")
)