package com.example.template.ux.edgetoedge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.datastore.AppPreferenceDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import org.lds.mobile.ext.stateInDefault
import javax.inject.Inject

@HiltViewModel
class EdgeToEdgeViewModel @Inject constructor(
    private val preferenceDataSource: AppPreferenceDataSource,
) : ViewModel() {
    val uiState = EdgeToEdgeUiState(
        enforceNavigationBarContrastFlow = preferenceDataSource.enforceNavigationBarContrastFlow.stateInDefault(viewModelScope, true),
        onToggleEnforceNavigationBarContrast = { preferenceDataSource.setEnforceNavigationBarContrastAsync(it) }
    )
}