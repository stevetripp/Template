package com.example.template.ux.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.datastore.AppPreferenceDataSource
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

class SettingsViewModel(
    private val preferenceDataSource: AppPreferenceDataSource,
) : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {
    private val inAppUpdateTypes = InAppUpdateType.entries

    val uiState = SettingsUiState(
        enforceNavigationBarContrastFlow = preferenceDataSource.enforceNavigationBarContrastFlow.stateInDefault(viewModelScope, true),
        inAppUpdateTypeFlow = preferenceDataSource.inAppUpdateTypeFlow.stateInDefault(viewModelScope, InAppUpdateType.NONE),
        onEnforceNavigationBarContrastClicked = { preferenceDataSource.setEnforceNavigationBarContrastAsync(it) },
        onInAppUpdateTypeClicked = {
            // Cycle through the enum values and persist selection
            val currentType = it
            val currentIndex = inAppUpdateTypes.indexOf(currentType)
            val nextIndex = (currentIndex + 1) % inAppUpdateTypes.size
            preferenceDataSource.setInAppUpdateTypeAsync(inAppUpdateTypes[nextIndex])
        }
    )
}