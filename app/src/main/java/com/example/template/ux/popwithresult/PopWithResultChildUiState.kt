package com.example.template.ux.popwithresult

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PopWithResultChildUiState(
    val valueFlow: StateFlow<String> = MutableStateFlow(""),
    val onValueChanged: (String) -> Unit = {},
    val onPopBackStack: () -> Unit = {}
)