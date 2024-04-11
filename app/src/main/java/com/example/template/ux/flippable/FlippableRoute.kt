package com.example.template.ux.flippable

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import com.example.template.ux.DeepLink
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation.NavRouteDefinition
import org.lds.mobile.navigation.asNavRoute
import org.lds.mobile.navigation.asNavRouteDefinition
import org.lds.mobile.ui.compose.navigation.NavComposeRoute

object FlippableRoute : NavComposeRoute() {
    override val routeDefinition: NavRouteDefinition = Screen.FLIPPABLE.name.asNavRouteDefinition()
    override fun getArguments(): List<NamedNavArgument> = emptyList()
    fun createRoute() = routeDefinition.value.asNavRoute()
    override fun getDeepLinks(): List<NavDeepLink> {
        return listOf(
            navDeepLink { uriPattern = "${DeepLink.ROOT}/${Screen.FLIPPABLE.name}" }
        )
    }
}