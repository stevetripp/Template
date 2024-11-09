package com.example.template.ux.breadcrumbs

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.example.template.ux.main.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation.NavigationRoute
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import javax.inject.Inject

@HiltViewModel
class BreadCrumbsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {

    private val breadCrumbsRoute = savedStateHandle.toRoute<BreadCrumbsRoute>(/*BreadCrumbsRoute.typeMap()*/)
    private val titleFlow = MutableStateFlow(breadCrumbsRoute.title ?: Screen.BREAD_CRUMBS_SCREEN.title)
    private val breadCrumbsFlow = MutableStateFlow<List<BreadCrumb>>(emptyList())

    val uiState = BreadCrumbsUiState(
        breadCrumbsFlow = breadCrumbsFlow,
        titleFlow = titleFlow,

        onBreadCrumbClicked = ::onBreadCrumbClicked,
        onNavigate = ::onNavigate,
        onNavController = ::onNavController
    )

    @SuppressLint("RestrictedApi")
    private fun onNavController(navController: NavController) {
        val backStackRoutes = navController.currentBackStack.value
        val routes = backStackRoutes.mapNotNull { it.getRoute() }
        breadCrumbsFlow.value = routes.take(routes.size - 1).filterIsInstance<BreadCrumbsRoute>().mapIndexed() { index, breadCrumbsRoute ->
            BreadCrumb(breadCrumbsRoute, breadCrumbsRoute.title)
        }
    }

    private fun NavBackStackEntry.getRoute(): NavigationRoute? {
        return when {
            destination.hasRoute(BreadCrumbsRoute::class) -> toRoute<BreadCrumbsRoute>()
            else -> null
        }
    }

    private fun onBreadCrumbClicked(breadCrumb: BreadCrumb) = popBackStack(breadCrumb.route)

    private fun onNavigate() {
        val breadCrumbs = breadCrumbsFlow.value.toMutableList()
        val nextIndex = breadCrumbs.size + 1
        navigate(BreadCrumbsRoute("${Screen.BREAD_CRUMBS_SCREEN.title} (${nextIndex})"))
    }
}
