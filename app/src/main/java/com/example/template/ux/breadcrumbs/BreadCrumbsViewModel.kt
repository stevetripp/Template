package com.example.template.ux.breadcrumbs

import androidx.lifecycle.ViewModel
import com.example.template.ux.main.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

@HiltViewModel
class BreadCrumbsViewModel @Inject constructor(
) : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    val uiState = BreadcrumbsUiState(
        onBreadCrumbClicked = ::onBreadCrumbClicked,
        onNavigate = ::onNavigate
    )

    private fun onBreadCrumbClicked(route: BreadcrumbRoute) = popBackStack(route)

    private fun onNavigate(breadcrumbRoutes: List<BreadcrumbRoute>) {
        val nextIndex = breadcrumbRoutes.size + 1
        navigate(BreadcrumbsRoute("${Screen.BREADCRUMBS_SCREEN.title} (${nextIndex})"))
    }
}
