@file:Suppress("unused")

package org.lds.mobile.navigation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Used for typical ViewModels that have navigation
 */
interface ViewModelNav {
    val navigationActionFlow: StateFlow<NavigationAction?>

    fun navigate(route: NavRoute, popBackStack: Boolean = false)
    fun navigate(routes: List<NavRoute>)
    fun navigate(route: NavRoute, navOptions: NavOptions)
    fun navigate(route: NavRoute, optionsBuilder: NavOptionsBuilder.() -> Unit)
    fun navigate(intent: Intent, options: Bundle? = null, popBackStack: Boolean = false)
    fun navigate(uri: Uri)
    fun popBackStack(popToRouteDefinition: NavRouteDefinition? = null, inclusive: Boolean = false)
    fun popBackStackWithResult(resultValues: List<PopResultKeyValue>, popToRouteDefinition: NavRouteDefinition? = null, inclusive: Boolean = false)

    fun navigate(navigationAction: NavigationAction)
    fun resetNavigate(navigationAction: NavigationAction)
}

class ViewModelNavImpl : ViewModelNav {
    private val _navigatorFlow = MutableStateFlow<NavigationAction?>(null)
    override val navigationActionFlow: StateFlow<NavigationAction?> = _navigatorFlow.asStateFlow()

    override fun navigate(route: NavRoute, popBackStack: Boolean) {
        _navigatorFlow.compareAndSet(null, if (popBackStack) NavigationAction.PopAndNavigate(route) else NavigationAction.Navigate(route))
    }

    override fun navigate(routes: List<NavRoute>) {
        _navigatorFlow.compareAndSet(null, NavigationAction.NavigateMultiple(routes))
    }

    override fun navigate(route: NavRoute, navOptions: NavOptions) {
        _navigatorFlow.compareAndSet(null, NavigationAction.NavigateWithOptions(route, navOptions))
    }

    override fun navigate(route: NavRoute, optionsBuilder: NavOptionsBuilder.() -> Unit) {
        _navigatorFlow.compareAndSet(null, NavigationAction.NavigateWithOptions(route, navOptions(optionsBuilder)))
    }

    override fun navigate(intent: Intent, options: Bundle?, popBackStack: Boolean) {
        _navigatorFlow.compareAndSet(null, if (popBackStack) NavigationAction.PopAndNavigateIntent(intent, options) else NavigationAction.NavigateIntent(intent, options))
    }

    override fun navigate(uri: Uri) {
        _navigatorFlow.compareAndSet(null, NavigationAction.NavigateUri(uri))
    }

    override fun popBackStack(popToRouteDefinition: NavRouteDefinition?, inclusive: Boolean) {
        _navigatorFlow.compareAndSet(null, NavigationAction.Pop(popToRouteDefinition, inclusive))
    }

    override fun popBackStackWithResult(resultValues: List<PopResultKeyValue>, popToRouteDefinition: NavRouteDefinition?, inclusive: Boolean) {
        _navigatorFlow.compareAndSet(null, NavigationAction.PopWithResult(resultValues, popToRouteDefinition, inclusive))
    }

    override fun navigate(navigationAction: NavigationAction) {
        _navigatorFlow.compareAndSet(null, navigationAction)
    }

    override fun resetNavigate(navigationAction: NavigationAction) {
        _navigatorFlow.compareAndSet(navigationAction, null)
    }
}