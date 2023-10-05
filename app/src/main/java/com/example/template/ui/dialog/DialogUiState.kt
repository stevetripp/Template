package com.example.template.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface DialogUiState<T> {
    val onConfirm: ((T) -> Unit)?
    val onDismiss: (() -> Unit)?
    val onDismissRequest: (() -> Unit)?
}

@Composable
fun <T : DialogUiState<*>> HandleDialogUiState(
    dialogUiStateFlow: StateFlow<T?>,
    dialog: @Composable (T) -> Unit = { dialogUiState -> LibraryDialogs(dialogUiState) }
) {
    val dialogUiState by dialogUiStateFlow.collectAsStateWithLifecycle()

    dialogUiState?.let {
        dialog(it)
    }
}

@Composable
fun LibraryDialogs(dialogUiState: DialogUiState<*>) {
    when (dialogUiState) {
        is ExampleDialogUiState -> ExampleDialog(dialogUiState)
        is EmptyStateDialogUiState -> EmptyStateDialog(uiState = dialogUiState)
        is ExampleAlertDialogUiState -> ExampleAlertDialog(uiState = dialogUiState)
    }
}

fun ViewModel.dismissDialog(
    dialogUiStateFlow: MutableStateFlow<DialogUiState<*>?>
) {
    dialogUiStateFlow.value = null
}
