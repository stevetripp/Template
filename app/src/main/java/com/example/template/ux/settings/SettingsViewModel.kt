package com.example.template.ux.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.datastore.AppPreferenceDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceDataSource: AppPreferenceDataSource,
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {
    val uiState = SettingsUiState(
        enforceNavigationBarContrastFlow = preferenceDataSource.enforceNavigationBarContrastFlow.stateInDefault(viewModelScope, true),
        onEnforceNavigationBarContrastClicked = { preferenceDataSource.setEnforceNavigationBarContrastAsync(it) },
    )
}