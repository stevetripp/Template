package com.example.template.ux.parameters

import com.example.template.domain.Parameter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DestinationUiState(
    val reqParam1Flow: StateFlow<Parameter> = MutableStateFlow(Parameter("")),
    val reqParam2Flow: StateFlow<EnumParameter> = MutableStateFlow(EnumParameter.ONE),
    val optParam1Flow: StateFlow<Parameter?> = MutableStateFlow(null),
    val optParam2Flow: StateFlow<EnumParameter?> = MutableStateFlow(null),
    val onCloseBack: Boolean = false,
)