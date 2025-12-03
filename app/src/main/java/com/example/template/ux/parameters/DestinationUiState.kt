package com.example.template.ux.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DestinationUiState(
    val reqParam1Flow: StateFlow<Parameter1> = MutableStateFlow(Parameter1("")),
    val reqParam2Flow: StateFlow<EnumParameter> = MutableStateFlow(EnumParameter.ONE),
    val optParam1Flow: StateFlow<Parameter1?> = MutableStateFlow(null),
    val optParam2Flow: StateFlow<EnumParameter?> = MutableStateFlow(null),
    val onCloseBack: Boolean = false,
)