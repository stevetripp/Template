package com.example.template.ux.parameters

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.template.ui.RouteUtil
import com.example.template.ui.navigation.NavComposeRoute

object DestinationRoute : NavComposeRoute() {

    private const val ROUTE = "DestinationRoute"

    override val routeDefinition = "$ROUTE/${RouteUtil.defineArg(Arg.REQUIRED)}?${RouteUtil.defineOptionalArgs(Arg.OPTIONAL)}"

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(Arg.REQUIRED) { type = NavType.StringType },
        navArgument(Arg.OPTIONAL) {
            type = NavType.StringType
            defaultValue = null
            nullable = true
        }
    )

    fun createRoute(param1: Parameter1, param2: Parameter2? = null) = "$ROUTE/$param1?${RouteUtil.optionalArgs(Arg.OPTIONAL to param2)}"

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
