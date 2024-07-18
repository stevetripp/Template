package com.example.template.ux.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ParametersUiState(
    val requiredValueFlow: MutableStateFlow<String> = MutableStateFlow(""),
    val enumParameterFlow: StateFlow<EnumParameter> = MutableStateFlow(EnumParameter.ONE),
    val optionalValueFlow: MutableStateFlow<String?> = MutableStateFlow(null),
    val optionalEnumParameterFlow: StateFlow<EnumParameter?> = MutableStateFlow(null),
    val onRequiredValueChanged: (String) -> Unit = {},
    val onEnumParameterChanged: (EnumParameter) -> Unit = {},
    val onOptionalValueChanged: (String) -> Unit = {},
    val onOptionalEnumParameterChanged: (EnumParameter) -> Unit = {},
    val onButtonClick: () -> Unit = {},
)