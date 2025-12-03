package com.example.template.ux.pullrefresh

import androidx.navigation3.runtime.NavKey
import com.example.template.util.DeepLink
import com.example.template.ux.main.Screen
import io.ktor.http.Url
import kotlinx.serialization.Serializable

@Serializable
data class PullRefreshRoute(val closeOnBack: Boolean = false) : NavKey

// https://trippntechnology.com/template/PULL_REFRESH
val PullRefreshRoute.Companion.deepLinkUrl get() = Url("${DeepLink.ROOT}/${Screen.PULL_REFRESH.name}")
