package com.example.template.ux.pullrefresh

import androidx.navigation.navDeepLink
import com.example.template.ux.DeepLink
import com.example.template.ux.main.Screen
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute

@Serializable
object PullRefreshRoute : NavigationRoute

fun PullRefreshRoute.deepLinks() = listOf(
    navDeepLink<PullRefreshRoute>(basePath = "${DeepLink.ROOT}/${Screen.PULL_REFRESH.name}",)
)