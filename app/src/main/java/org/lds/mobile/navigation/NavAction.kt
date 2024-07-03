@file:Suppress("unused")

package org.lds.mobile.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import co.touchlab.kermit.Logger

sealed interface NavActionRoute : NavAction {
    /**
     * @return true if navController.popBackStack() called AND was successful
     */
    fun navigate(navController: NavController, resetNavigate: (NavAction) -> Unit): Boolean
}

sealed interface NavActionIntent : NavAction {
    /**
     * @return true if navController.popBackStack() called AND was successful
     */
    fun navigate(context: Context, resetNavigate: (NavAction) -> Unit): Boolean
}

sealed interface NavActionFull : NavAction {
    /**
     * @return true if navController.popBackStack() called AND was successful
     */
    fun navigate(context: Context, navController: NavController, resetNavigate: (NavAction) -> Unit): Boolean
}


sealed interface NavAction {
    data class Navigate(private val route: NavRoute) : NavActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavAction) -> Unit): Boolean {
            navController.navigate(route.value)

            resetNavigate(this)
            return false
        }
    }

    data class NavigateMultiple(private val routes: List<NavRoute>) : NavActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavAction) -> Unit): Boolean {
            routes.forEach { route ->
                navController.navigate(route.value)
            }

            resetNavigate(this)
            return false
        }
    }

    data class NavigateWithOptions(private val route: NavRoute, private val navOptions: NavOptions) : NavActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavAction) -> Unit): Boolean {
            navController.navigate(route.value, navOptions)

            resetNavigate(this)
            return false
        }
    }

    data class NavigateIntent(private val intent: Intent, private val options: Bundle? = null) : NavActionIntent {
        override fun navigate(context: Context, resetNavigate: (NavAction) -> Unit): Boolean {
            try {
                context.startActivity(intent, options)
            } catch (ignore: Exception) {
                Logger.e(ignore) { "Failed to startActivity for intent (${intent.data})" }
            }
            resetNavigate(this)
            return false
        }
    }

    data class PopAndNavigate(private val route: NavRoute) : NavActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavAction) -> Unit): Boolean {
            val stackPopped = navController.popBackStack()
            navController.navigate(route.value)

            resetNavigate(this)
            return stackPopped
        }
    }

    data class PopAndNavigateIntent(private val intent: Intent, private val options: Bundle? = null) : NavActionFull {
        override fun navigate(context: Context, navController: NavController, resetNavigate: (NavAction) -> Unit): Boolean {
            val stackPopped = navController.popBackStack()
            try {
                context.startActivity(intent, options)
            } catch (ignore: Exception) {
                Logger.e(ignore) { "Failed to startActivity for intent (${intent.data})" }
            }
            resetNavigate(this)
            return stackPopped
        }
    }

    data class Pop(private val popToRouteDefinition: NavRouteDefinition? = null, private val inclusive: Boolean = false) : NavActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavAction) -> Unit): Boolean {
            val stackPopped = if (popToRouteDefinition == null) {
                navController.popBackStack()
            } else {
                navController.popBackStack(popToRouteDefinition.value, inclusive = inclusive)
            }

            resetNavigate(this)
            return stackPopped
        }
    }

    data class PopWithResult(
        private val resultValues: List<PopResultKeyValue>,
        private val popToRouteDefinition: NavRouteDefinition? = null,
        private val inclusive: Boolean = false,
    ) : NavActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavAction) -> Unit): Boolean {
            val destinationNavBackStackEntry = if (popToRouteDefinition != null) {
                navController.getBackStackEntry(popToRouteDefinition.value)
            } else {
                navController.previousBackStackEntry
            }
            resultValues.forEach { destinationNavBackStackEntry?.savedStateHandle?.set(it.key, it.value) }
            val stackPopped = if (popToRouteDefinition == null) {
                navController.popBackStack()
            } else {
                navController.popBackStack(popToRouteDefinition.value, inclusive = inclusive)
            }

            resetNavigate(this)
            return stackPopped
        }
    }
}

data class PopResultKeyValue(val key: String, val value: Any)

fun NavAction.navigate(context: Context, navController: NavController, resetNavigate: (NavAction) -> Unit) {
    when(this) {
        is NavActionIntent -> navigate(context, resetNavigate)
        is NavActionRoute -> navigate(navController, resetNavigate)
        is NavActionFull -> navigate(context, navController, resetNavigate)
    }
}