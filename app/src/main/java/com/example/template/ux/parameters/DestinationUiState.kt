package com.example.template.ux.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DestinationUiState(
    val requiredFlow: StateFlow<String> = MutableStateFlow(""),
    val optionalFlow: StateFlow<String?> = MutableStateFlow(null),
)