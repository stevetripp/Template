package com.example.template.ux.modalsidesheet

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ModalSideSheetUiState(
    val isExpandedFlow: StateFlow<Boolean> = MutableStateFlow(false),
    val onExpandClicked: () -> Unit = {},
    val onCloseClicked: () -> Unit = {},
)