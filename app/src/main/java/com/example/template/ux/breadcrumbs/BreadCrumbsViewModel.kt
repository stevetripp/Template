package com.example.template.ux.breadcrumbs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.template.util.SmtLogger
import com.example.template.ux.main.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import javax.inject.Inject

@HiltViewModel
class BreadCrumbsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {

    private val breadCrumbsRoute = savedStateHandle.toRoute<BreadCrumbsRoute>(BreadCrumbsRoute.typeMap())
    private val levelFlow = MutableStateFlow(breadCrumbsRoute.level)

    private val titleFlow = levelFlow.map {
        val sb = StringBuilder(Screen.BREAD_CRUMBS_SCREEN.title)
        if (it > 0) sb.append(" $it")
        sb.toString()
    }.stateInDefault(viewModelScope, "")

    val uiState = BreadCrumbsUiState(
        titleFlow = titleFlow,
        onNavigate = {
            val breadCrumbs = (breadCrumbsRoute.breadCrumbs ?: emptyList<BreadCrumb>()).toMutableList()
            breadCrumbs.add(BreadCrumb(breadCrumbsRoute, titleFlow.value))
            navigate(BreadCrumbsRoute(breadCrumbsRoute.level + 1, breadCrumbs))
        }
    )

    init {
        SmtLogger.i("""Bread Crumbs: ${breadCrumbsRoute.breadCrumbs}""")
    }
}
