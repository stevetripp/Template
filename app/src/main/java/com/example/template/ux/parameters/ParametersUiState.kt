package com.example.template.ux.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ParametersUiState(
    val requiredValueFlow: StateFlow<String> = MutableStateFlow(""),
    val optionalValueFlow: StateFlow<String> = MutableStateFlow(""),
    val onRequiredValueChanged: (String) -> Unit = {},
    val onOptionalValueChanged: (String) -> Unit = {},
    val onButtonClick: () -> Unit = {},
)