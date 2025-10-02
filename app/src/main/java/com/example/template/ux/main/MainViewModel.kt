package com.example.template.ux.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.template.model.datastore.AppPreferenceDataSource
import com.example.template.util.SmtLogger
import com.example.template.ux.breadcrumbs.BreadcrumbManager
import com.example.template.ux.settings.SettingRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation.DefaultNavigationBarConfig
import org.lds.mobile.navigation.ViewModelNavigationBar
import org.lds.mobile.navigation.ViewModelNavigationBarImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val breadcrumbManager: BreadcrumbManager,
    preferenceDataSource: AppPreferenceDataSource,
) : ViewModel(), ViewModelNavigationBar<NavBarItem> by ViewModelNavigationBarImpl(NavBarItem.UI_EXAMPLES, DefaultNavigationBarConfig(NavBarItem.getNavBarItemRouteMap())) {

    lateinit var navController: NavController

    val uiState = MainUiState(
        enforceNavigationBarContrastFlow = preferenceDataSource.enforceNavigationBarContrastFlow.stateInDefault(viewModelScope, true),
        onSettingsClicked = { navigate(SettingRoute) },
    )

    fun initBreadcrumbManager(navController: NavController) {
        this.navController = navController
        breadcrumbManager.initNavController(navController)
    }

    fun navigateToDeepLink(uri: Uri) {
        SmtLogger.i("""uri: $uri""")
        navController.navigate(uri)
    }
}