package com.example.template.ux.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.datastore.AppPreferenceDataSource
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
    private val preferenceDataSource: AppPreferenceDataSource,
) : ViewModel(), ViewModelNavigationBar<NavBarItem> by ViewModelNavigationBarImpl(NavBarItem.UI_EXAMPLES, DefaultNavigationBarConfig(NavBarItem.getNavBarItemRouteMap())) {
    val uiState = MainUiState(
        enforceNavigationBarContrastFlow = preferenceDataSource.enforceNavigationBarContrastFlow.stateInDefault(viewModelScope, true),
        onSettingsClicked = { navigate(SettingRoute) },
    )
}