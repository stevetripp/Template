package com.example.template.ux.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.ext.stateInDefault
import com.example.template.model.datastore.AppPreferenceDataSource
import com.example.template.ui.navigation.ViewModelNav
import com.example.template.ui.navigation.ViewModelNavImpl
import com.example.template.ui.widget.PermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel
@Inject constructor(
    private val appPreferenceDataSource: AppPreferenceDataSource,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    val uiState = PermissionsUiState(
        permissionStateFlow = appPreferenceDataSource.locationPermissionStateFlow.stateInDefault(viewModelScope, PermissionState.NotPermitted),
        updatePermissionState = { appPreferenceDataSource.setLocationPermissionStateAsync(it) }
    )
}