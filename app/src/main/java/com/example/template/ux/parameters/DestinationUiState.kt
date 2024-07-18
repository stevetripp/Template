package com.example.template.ux.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DestinationUiState(
    val requiredFlow: StateFlow<Parameter1> = MutableStateFlow(Parameter1("")),
    val enumParameter: StateFlow<EnumParameter> = MutableStateFlow(EnumParameter.ONE),
    val optionalFlow: StateFlow<Parameter2?> = MutableStateFlow(null),
)