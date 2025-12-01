package com.example.template.ux.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MainUiState(
    val enforceNavigationBarContrastFlow: StateFlow<Boolean> = MutableStateFlow(true),
)