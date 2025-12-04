package com.example.template.ux.popwithresult

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PopWithResultParentUiState(
    val resultStringFlow: StateFlow<String?> = MutableStateFlow(null),
    val onClickMeClicked: () -> Unit = {},
)