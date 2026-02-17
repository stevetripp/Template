package com.example.template.ux.deeplink

import androidx.navigation3.runtime.NavKey
import org.ics.mobile.commons.Uri
import org.ics.mobile.commons.toUri
import org.lds.mobile.navigation3.router.BaseDeepLinkRouter
import org.lds.mobile.navigation3.router.RouteMatcher

object CatalogDeepLinkRouter : BaseDeepLinkRouter() {
    override fun getMatchers(): List<RouteMatcher<out NavKey>> = listOf(
        DeepLinkRouteMatcher,
    )
}

object DeepLinkRouteMatcher : RouteMatcher<DeepLinkRoute>("/DEEPLINK".toUri()) {
    private object Arg {
        const val REQUIRED_PATH_INDEX = 1
        const val OPTIONAL_PARAM_NAME = "optionalValue"
    }

    override fun parse(uri: Uri): DeepLinkRoute? {
        val paths = uri.pathSegments
        val requiredValue = paths.getOrNull(Arg.REQUIRED_PATH_INDEX)
        val optionalValue = uri.getQueryParameter(Arg.OPTIONAL_PARAM_NAME)

        return requiredValue?.let {
            DeepLinkRoute(
                requiredValue = requiredValue,
                optionalValue = optionalValue
            )
        }
    }

    override fun matchesKey(route: NavKey): Boolean = route is DeepLinkRoute

    override fun toUri(route: DeepLinkRoute): String {
        return baseUri.newBuilder()
            .appendPath(route.requiredValue)
            .appendQueryParameter(Arg.OPTIONAL_PARAM_NAME, route.optionalValue)
            .build()
            .toString()
    }
}
