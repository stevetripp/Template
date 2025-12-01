package com.example.template.ux.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.datastore.AppPreferenceDataSource
import com.example.template.util.SmtLogger
import com.example.template.ux.breadcrumbs.BreadcrumbManager
import com.example.template.ux.settings.InAppUpdateType
import com.example.template.ux.settings.SettingRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation3.DefaultNavigation3BarConfig
import org.lds.mobile.navigation3.ViewModelNavigation3Bar
import org.lds.mobile.navigation3.ViewModelNavigation3BarImpl
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val breadcrumbManager: BreadcrumbManager,
    preferenceDataSource: AppPreferenceDataSource,
) : ViewModel(), ViewModelNavigation3Bar<NavBarItem> by ViewModelNavigation3BarImpl(NavBarItem.UI_EXAMPLES, DefaultNavigation3BarConfig(NavBarItem.getNavBarItemRouteMap())) {

    val inAppUpdateType: InAppUpdateType = runBlocking { preferenceDataSource.inAppUpdateTypeFlow.first() }

    val uiState = MainUiState(
        enforceNavigationBarContrastFlow = preferenceDataSource.enforceNavigationBarContrastFlow.stateInDefault(viewModelScope, true),
        onSettingsClicked = {
            SmtLogger.i("""Here""")
            navigate(SettingRoute) },
    )

    fun initBreadcrumbManager(navigator: Navigation3Navigator) {}// = breadcrumbManager.initNavigationState(navController)
}