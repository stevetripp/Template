package com.example.template.ux.parameters

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.template.ui.RouteUtil
import com.example.template.ux.DeepLink
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation.NavRoute
import org.lds.mobile.navigation.NavRouteDefinition
import org.lds.mobile.ui.compose.navigation.NavComposeRoute

object DestinationRoute : NavComposeRoute() {

    private const val ROUTE = "DestinationRoute"

    override val routeDefinition = NavRouteDefinition("$ROUTE/${RouteUtil.defineArg(Arg.REQUIRED)}?${RouteUtil.defineOptionalArgs(Arg.OPTIONAL)}")

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(Arg.REQUIRED) { type = NavType.StringType },
        navArgument(Arg.OPTIONAL) {
            type = NavType.StringType
            defaultValue = null
            nullable = true
        }
    )

    override fun getDeepLinks(): List<NavDeepLink> {
        return listOf(
            navDeepLink { uriPattern = "${DeepLink.ROOT}/${Screen.PARAMETERS.name}/$ROUTE/${RouteUtil.defineArg(Arg.REQUIRED)}?${RouteUtil.defineOptionalArgs(Arg.OPTIONAL)}" }
        )
    }

    fun createRoute(param1: Parameter1, param2: Parameter2? = null) = NavRoute("$ROUTE/$param1?${RouteUtil.optionalArgs(Arg.OPTIONAL to param2)}")

    private object Arg {
        const val REQUIRED = "REQUIRED"
        const val OPTIONAL = "OPTIONAL"
    }

    data class Args(val param1: Parameter1, val param2: Parameter2?)

    fun getArgs(savedStateHandle: SavedStateHandle) = Args(
        param1 = Parameter1(requireNotNull(savedStateHandle[Arg.REQUIRED])),
        param2 = savedStateHandle.get<String?>(Arg.OPTIONAL)?.let { Parameter2(it) }
    )
}

@JvmInline
value class Parameter1(val value: String) {
    init {
        require(value.isNotBlank())
    }

    override fun toString() = value
}

@JvmInline
value class Parameter2(val value: String) {
    init {
        require(value.isNotBlank())
    }

    override fun toString() = value
}
