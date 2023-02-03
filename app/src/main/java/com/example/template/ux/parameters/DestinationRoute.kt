package com.example.template.ux.parameters

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.template.ui.RouteUtil
import com.example.template.ui.navigation.NavComposeRoute

object DestinationRoute : NavComposeRoute() {

    private const val ROUTE = "DestinationRoute"

    override val routeDefinition = "$ROUTE/${RouteUtil.defineArg(Args.REQUIRED)}?${RouteUtil.defineOptionalArgs(Args.OPTIONAL)}"

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(Args.REQUIRED) { type = NavType.StringType },
        navArgument(Args.OPTIONAL) {
            type = NavType.StringType
            defaultValue = null
            nullable = true
        }
    )

    fun createRoute(required: String, optional: String? = null) = "$ROUTE/$required?${RouteUtil.optionalArgs(Args.OPTIONAL to optional)}"

    object Args {
        const val REQUIRED = "REQUIRED"
        const val OPTIONAL = "OPTIONAL"
    }
}