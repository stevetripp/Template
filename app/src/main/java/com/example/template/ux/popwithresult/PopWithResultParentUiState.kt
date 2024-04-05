package com.example.template.ux.popwithresult

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PopWithResultParentUiState(
    val resultStringFlow: StateFlow<String?> = MutableStateFlow(null),
    val onClickMeClicked: () -> Unit = {},
    val onSetNavController: (NavController) -> Unit = {},
)