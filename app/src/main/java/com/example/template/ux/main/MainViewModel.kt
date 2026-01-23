package com.example.template.ux.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.datastore.AppPreferenceDataSource
import com.example.template.ux.settings.InAppUpdateType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.lds.mobile.ext.stateInDefault

class MainViewModel(
    preferenceDataSource: AppPreferenceDataSource,
) : ViewModel() {

    val inAppUpdateType: InAppUpdateType = runBlocking { preferenceDataSource.inAppUpdateTypeFlow.first() }

    val uiState = MainUiState(
        enforceNavigationBarContrastFlow = preferenceDataSource.enforceNavigationBarContrastFlow.stateInDefault(viewModelScope, true),
    )
}