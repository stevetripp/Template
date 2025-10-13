package com.example.template.ux.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SettingsUiState(
    val enforceNavigationBarContrastFlow: StateFlow<Boolean> = MutableStateFlow(true),
    val inAppUpdateTypeFlow: StateFlow<InAppUpdateType> = MutableStateFlow(InAppUpdateType.NONE),
    val onEnforceNavigationBarContrastClicked: (Boolean) -> Unit = {},
    val onInAppUpdateTypeClicked: (InAppUpdateType) -> Unit = {},
)