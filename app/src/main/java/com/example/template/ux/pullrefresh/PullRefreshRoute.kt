package com.example.template.ux.pullrefresh

import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey
import com.example.template.ux.DeepLink
import com.example.template.ux.main.Screen
import kotlinx.serialization.Serializable

@Serializable
data class PullRefreshRoute(val closeOnBack: Boolean = false) : NavKey

// https://trippntechnology.com/template/PullRefreshRoute
val PullRefreshRoute.Companion.deepLinkUri get() = "${DeepLink.ROOT}/${Screen.PULL_REFRESH.name}".toUri()
