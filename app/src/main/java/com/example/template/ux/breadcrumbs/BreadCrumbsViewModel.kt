package com.example.template.ux.breadcrumbs

import com.example.template.ux.main.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

@HiltViewModel(assistedFactory = BreadCrumbsViewModel.Factory::class)
class BreadCrumbsViewModel
@AssistedInject constructor(
   @Assisted override val breadcrumbRoutes: List<BreadcrumbRoute>
) : BreadcrumbViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    val uiState = BreadcrumbsUiState(
        breadcrumbRoutes = breadcrumbRoutes,
        onBreadCrumbClicked = ::onBreadCrumbClicked,
        onNavigate = ::onNavigate
    )

    private fun onBreadCrumbClicked(route: BreadcrumbRoute) = popBackStack(route)

    private fun onNavigate() {
        val nextIndex = breadcrumbRoutes.size + 1
        navigate(BreadcrumbsRoute("${Screen.BREADCRUMBS_SCREEN.title} (${nextIndex})"))
    }

    @AssistedFactory
    interface Factory {
        fun create(breadcrumbRoutes: List<BreadcrumbRoute>): BreadCrumbsViewModel
    }
}
