package com.example.template.ux.nesteddeeplink

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation.NavRoute
import org.lds.mobile.navigation.NavRouteDefinition
import org.lds.mobile.navigation.RouteUtil
import org.lds.mobile.navigation.asNavRoute
import org.lds.mobile.ui.compose.navigation.NavComposeRoute

object Level2Route : NavComposeRoute() {
    private val ROUTE = Screen.LEVEL_TWO.name

    override val routeDefinition: NavRouteDefinition = NavRouteDefinition("$ROUTE?${RouteUtil.defineOptionalArgs(Arg.DEEP_LINK_ROUTE)}")
    override fun getArguments(): List<NamedNavArgument> {
        return listOf(
            navArgument(Arg.DEEP_LINK_ROUTE) {
                type = NavType.StringType
                nullable = true
            }
        )
    }

    fun createRoute(deepLinkRoute: NavRoute? = null): NavRoute {
        val route = if (deepLinkRoute == ROUTE.asNavRoute()) null else deepLinkRoute
        return "$ROUTE?${RouteUtil.optionalArgs(Arg.DEEP_LINK_ROUTE to route)}".asNavRoute()
    }

    object Arg {
        const val DEEP_LINK_ROUTE = "DEEP_LINK_ROUTE"
    }

    data class Args(val deepLinkRoute: NavRoute?)

    fun getArgs(savedStateHandle: SavedStateHandle) = Args(savedStateHandle.get<String>(Arg.DEEP_LINK_ROUTE)?.let { NavRoute(it) })
}