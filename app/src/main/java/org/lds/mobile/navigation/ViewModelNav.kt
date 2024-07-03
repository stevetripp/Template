@file:Suppress("unused")

package org.lds.mobile.navigation

import android.content.Intent
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
    val navigationActionFlow: StateFlow<NavAction?>

    fun navigate(route: NavRoute, popBackStack: Boolean = false)
    fun navigate(routes: List<NavRoute>)
    fun navigate(route: NavRoute, navOptions: NavOptions)
    fun navigate(route: NavRoute, optionsBuilder: NavOptionsBuilder.() -> Unit)
    fun navigate(intent: Intent, options: Bundle? = null, popBackStack: Boolean = false)
    fun popBackStack(popToRouteDefinition: NavRouteDefinition? = null, inclusive: Boolean = false)
    fun popBackStackWithResult(resultValues: List<PopResultKeyValue>, popToRouteDefinition: NavRouteDefinition? = null, inclusive: Boolean = false)

    fun navigate(navAction: NavAction)
    fun resetNavigate(navAction: NavAction)
}

class ViewModelNavImpl : ViewModelNav {
    private val _navigatorFlow = MutableStateFlow<NavAction?>(null)
    override val navigationActionFlow: StateFlow<NavAction?> = _navigatorFlow.asStateFlow()

    override fun navigate(route: NavRoute, popBackStack: Boolean) {
        _navigatorFlow.compareAndSet(null, if (popBackStack) NavAction.PopAndNavigate(route) else NavAction.Navigate(route))
    }

    override fun navigate(routes: List<NavRoute>) {
        _navigatorFlow.compareAndSet(null, NavAction.NavigateMultiple(routes))
    }

    override fun navigate(route: NavRoute, navOptions: NavOptions) {
        _navigatorFlow.compareAndSet(null, NavAction.NavigateWithOptions(route, navOptions))
    }

    override fun navigate(route: NavRoute, optionsBuilder: NavOptionsBuilder.() -> Unit) {
        _navigatorFlow.compareAndSet(null, NavAction.NavigateWithOptions(route, navOptions(optionsBuilder)))
    }

    override fun navigate(intent: Intent, options: Bundle?, popBackStack: Boolean) {
        _navigatorFlow.compareAndSet(null, if (popBackStack) NavAction.PopAndNavigateIntent(intent, options) else NavAction.NavigateIntent(intent, options))
    }

    override fun popBackStack(popToRouteDefinition: NavRouteDefinition?, inclusive: Boolean) {
        _navigatorFlow.compareAndSet(null, NavAction.Pop(popToRouteDefinition, inclusive))
    }

    override fun popBackStackWithResult(resultValues: List<PopResultKeyValue>, popToRouteDefinition: NavRouteDefinition?, inclusive: Boolean) {
        _navigatorFlow.compareAndSet(null, NavAction.PopWithResult(resultValues, popToRouteDefinition, inclusive))
    }

    override fun navigate(navAction: NavAction) {
        _navigatorFlow.compareAndSet(null, navAction)
    }

    override fun resetNavigate(navAction: NavAction) {
        _navigatorFlow.compareAndSet(navAction, null)
    }
}
