package com.example.template.ux.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ParametersUiState(
    val reqParam1Flow: MutableStateFlow<String> = MutableStateFlow(""),
    val reqParam2Flow: StateFlow<EnumParameter> = MutableStateFlow(EnumParameter.ONE),
    val optParam1Flow: MutableStateFlow<String?> = MutableStateFlow(null),
    val optParam2Flow: StateFlow<EnumParameter?> = MutableStateFlow(null),
    val onReqParam1Changed: (String) -> Unit = {},
    val onReqParam2Changed: (EnumParameter) -> Unit = {},
    val onOptParam1Changed: (String) -> Unit = {},
    val onOptParam2Changed: (EnumParameter) -> Unit = {},
    val onButtonClick: () -> Unit = {},
)