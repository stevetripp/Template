package com.example.template.ux.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ParametersUiState(
    val requiredValueFlow: StateFlow<String> = MutableStateFlow(""),
    val enumParameterFlow: StateFlow<EnumParameter> = MutableStateFlow(EnumParameter.ONE),
    val optionalValueFlow: StateFlow<String?> = MutableStateFlow(null),
    val onRequiredValueChanged: (String) -> Unit = {},
    val onEnumParameterChanged: (EnumParameter) -> Unit = {},
    val onOptionalValueChanged: (String) -> Unit = {},
    val onButtonClick: () -> Unit = {},
)