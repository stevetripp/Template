package com.example.template.ux.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SettingsUiState(
    val enforceNavigationBarContrastFlow: StateFlow<Boolean> = MutableStateFlow(true),
    val onEnforceNavigationBarContrastClicked: (Boolean) -> Unit = {},
)