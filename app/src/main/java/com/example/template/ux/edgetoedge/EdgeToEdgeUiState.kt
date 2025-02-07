package com.example.template.ux.edgetoedge

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class EdgeToEdgeUiState(
    var enforceNavigationBarContrastFlow: StateFlow<Boolean> = MutableStateFlow(true),
    var onToggleEnforceNavigationBarContrast: (Boolean) -> Unit = {},
)