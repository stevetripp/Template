package com.example.template.ux.dialog

import com.example.template.ui.dialog.DialogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DialogScreenUiState(
    val dialogUiStateFlow: StateFlow<DialogUiState<*>?> = MutableStateFlow<DialogUiState<*>?>(null),
    val onDialogClicked: () -> Unit = {},
    val onEmptyStateDialogClicked: () -> Unit = {},
    val onAlertDialogClicked: () -> Unit = {},
)