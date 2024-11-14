package com.example.template.ux.breadcrumbs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.template.ux.main.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import javax.inject.Inject

@HiltViewModel
class BreadCrumbsViewModel @Inject constructor(
    breadcrumbManager: BreadcrumbManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {

    private val breadCrumbsRoute = savedStateHandle.toRoute<BreadcrumbsRoute>(/*BreadCrumbsRoute.typeMap()*/)
    private val titleFlow = MutableStateFlow(breadCrumbsRoute.title)
    private val breadcrumbRoutesFlow = breadcrumbManager.breadcrumbRoutesFlow().stateInDefault(viewModelScope, emptyList())

    val uiState = BreadcrumbsUiState(
        breadcrumbRoutesFlow = breadcrumbRoutesFlow,
        titleFlow = titleFlow,

        onBreadCrumbClicked = ::onBreadCrumbClicked,
        onNavigate = ::onNavigate
    )

    private fun onBreadCrumbClicked(route: BreadcrumbRoute) = popBackStack(route)

    private fun onNavigate() {
        val breadCrumbs = breadcrumbRoutesFlow.value.toMutableList()
        val nextIndex = breadCrumbs.size + 1
        navigate(BreadcrumbsRoute("${Screen.BREADCRUMBS_SCREEN.title} (${nextIndex})"))
    }
}
