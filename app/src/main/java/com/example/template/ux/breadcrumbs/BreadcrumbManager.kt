package com.example.template.ux.breadcrumbs

import android.annotation.SuppressLint
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreadcrumbManager @Inject constructor() {

    private lateinit var navController: NavController

    fun initNavController(navController: NavController) {
        this.navController = navController
    }

    @SuppressLint("RestrictedApi")
    fun breadcrumbRoutesFlow(): Flow<List<BreadcrumbRoute>> = navController.currentBackStack.map { backStackRoutes ->
        val routes = backStackRoutes.mapNotNull { entry -> entry.getRoute() }
        if (routes.size > 1) routes.take(routes.size - 1) else emptyList()
    }

    private fun NavBackStackEntry.getRoute(): BreadcrumbRoute? {
        return when {
            destination.hasRoute<BreadcrumbsRoute>() -> toRoute<BreadcrumbsRoute>()
            else -> null
        }
    }
}