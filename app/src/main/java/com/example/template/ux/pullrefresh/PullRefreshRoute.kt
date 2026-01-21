package com.example.template.ux.pullrefresh

import androidx.navigation3.runtime.NavKey
import com.example.template.util.DeepLinkConstants
import com.example.template.util.ext.addPathSegments
import com.example.template.ux.main.Screen
import io.ktor.http.Url
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation3.DeepLinkPattern

@Serializable
data class PullRefreshRoute(val closeOnBack: Boolean = false) : NavKey

val PullRefreshRoute.Companion.deepLinkPatterns
    get() = listOf(
        DeepLinkPattern(Url(DeepLinkConstants.HTTPS_ROOT))
            .addPathSegments(DeepLinkConstants.PATH_PREFIX, Screen.PULL_REFRESH.name),
        DeepLinkPattern(Url(DeepLinkConstants.CUSTOM_ROOT))
            .addPathSegments(Screen.PULL_REFRESH.name)
    )
