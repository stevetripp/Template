@file:Suppress("unused")

package org.lds.mobile.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import co.touchlab.kermit.Logger

sealed interface NavigationActionRoute : NavigationAction {
    /**
     * @return true if navController.popBackStack() called AND was successful
     */
    fun navigate(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean
}

sealed interface NavigationActionIntent : NavigationAction {
    /**
     * @return true if navController.popBackStack() called AND was successful
     */
    fun navigate(context: Context, resetNavigate: (NavigationAction) -> Unit): Boolean
}

sealed interface NavigationActionFull : NavigationAction {
    /**
     * @return true if navController.popBackStack() called AND was successful
     */
    fun navigate(context: Context, navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean
}


sealed interface NavigationAction {
    data class Navigate(private val route: NavigationRoute) : NavigationActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            navController.navigate(route)

            resetNavigate(this)
            return false
        }
    }

    data class NavigateMultiple(private val routes: List<NavigationRoute>) : NavigationActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            routes.forEach { route ->
                navController.navigate(route)
            }

            resetNavigate(this)
            return false
        }
    }

    data class NavigateWithOptions(private val route: NavigationRoute, private val navOptions: NavOptions) : NavigationActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            navController.navigate(route, navOptions)

            resetNavigate(this)
            return false
        }
    }

    data class NavigateIntent(private val intent: Intent, private val options: Bundle? = null) : NavigationActionIntent {
        override fun navigate(context: Context, resetNavigate: (NavigationAction) -> Unit): Boolean {
            try {
                context.startActivity(intent, options)
            } catch (ignore: Exception) {
                Logger.e(ignore) { "Failed to startActivity for intent (${intent.data})" }
            }
            resetNavigate(this)
            return false
        }
    }

    data class PopAndNavigate(private val route: NavigationRoute) : NavigationActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            val stackPopped = navController.popBackStack()
            navController.navigate(route)

            resetNavigate(this)
            return stackPopped
        }
    }

    data class PopAndNavigateIntent(private val intent: Intent, private val options: Bundle? = null) : NavigationActionFull {
        override fun navigate(context: Context, navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
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

    data class Pop(private val route: NavigationRoute? = null, private val inclusive: Boolean = false) : NavigationActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            val stackPopped = if (route == null) {
                navController.popBackStack()
            } else {
                navController.popBackStack(route, inclusive = inclusive)
            }

            resetNavigate(this)
            return stackPopped
        }
    }

    data class PopToDestination(private val destination: String, private val inclusive: Boolean = false) : NavigationActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            val stackPopped = navController.popBackStack(destination, inclusive = inclusive)
            resetNavigate(this)
            return stackPopped
        }
    }

    data class PopWithResult(
        private val resultValues: List<PopResultKeyValue>,
        private val popToRoute: NavigationRoute? = null,
        private val inclusive: Boolean = false,
    ) : NavigationActionRoute {
        override fun navigate(navController: NavController, resetNavigate: (NavigationAction) -> Unit): Boolean {
            val destinationNavBackStackEntry = if (popToRoute != null) {
                navController.getBackStackEntry(popToRoute)
            } else {
                navController.previousBackStackEntry
            }
            resultValues.forEach { destinationNavBackStackEntry?.savedStateHandle?.set(it.key, it.value) }
            val stackPopped = if (popToRoute == null) {
                navController.popBackStack()
            } else {
                navController.popBackStack(popToRoute, inclusive = inclusive)
            }

            resetNavigate(this)
            return stackPopped
        }
    }
}

fun NavigationAction.navigate(context: Context, navController: NavController, resetNavigate: (NavigationAction) -> Unit) {
    when (this) {
        is NavigationActionIntent -> navigate(context, resetNavigate)
        is NavigationActionRoute -> navigate(navController, resetNavigate)
        is NavigationActionFull -> navigate(context, navController, resetNavigate)
    }
}