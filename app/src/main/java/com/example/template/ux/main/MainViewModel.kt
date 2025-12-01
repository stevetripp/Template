package com.example.template.ux.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.datastore.AppPreferenceDataSource
import com.example.template.ux.settings.InAppUpdateType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation3.DefaultNavigation3BarConfig
import org.lds.mobile.navigation3.ViewModelNavigation3Bar
import org.lds.mobile.navigation3.ViewModelNavigation3BarImpl

@HiltViewModel
class MainViewModel
@Inject constructor(
    preferenceDataSource: AppPreferenceDataSource,
) : ViewModel(), ViewModelNavigation3Bar<NavBarItem> by ViewModelNavigation3BarImpl(NavBarItem.UI_EXAMPLES, DefaultNavigation3BarConfig(NavBarItem.getNavBarItemRouteMap())) {

    val inAppUpdateType: InAppUpdateType = runBlocking { preferenceDataSource.inAppUpdateTypeFlow.first() }

    val uiState = MainUiState(
        enforceNavigationBarContrastFlow = preferenceDataSource.enforceNavigationBarContrastFlow.stateInDefault(viewModelScope, true),
    )
}