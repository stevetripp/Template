package com.example.template.ux.popwithresult

import androidx.navigation.NamedNavArgument
import org.lds.mobile.navigation.NavRoute
import org.lds.mobile.navigation.NavRouteDefinition
import org.lds.mobile.ui.compose.navigation.NavComposeRoute

object PopWithResultChildRoute : NavComposeRoute() {
    override val routeDefinition: NavRouteDefinition = NavRouteDefinition("PopWithResultChildRoute")
    override fun getArguments(): List<NamedNavArgument> = emptyList()

    fun createRoute() = NavRoute(routeDefinition.value)

    object Arg {
        const val RESULT_STRING = "RESULT_STRING"
    }
}