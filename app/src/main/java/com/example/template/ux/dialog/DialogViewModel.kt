package com.example.template.ux.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.lifecycle.ViewModel
import com.example.template.ui.dialog.DialogUiState
import com.example.template.ui.dialog.EmptyStateDialogUiState
import com.example.template.ui.dialog.ExampleAlertDialogUiState
import com.example.template.ui.dialog.ExampleDialogUiState
import com.example.template.ui.dialog.dismissDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DialogViewModel
@Inject constructor(
) : ViewModel() {

    private val dialogUiStateFlow: MutableStateFlow<DialogUiState<*>?> = MutableStateFlow<DialogUiState<*>?>(null)

    val uiState = DialogScreenUiState(
        dialogUiStateFlow = dialogUiStateFlow,
        onDialogClicked = ::onDialogClicked,
        onEmptyStateDialogClicked = ::onCustomDialogClicked,
        onAlertDialogClicked = ::onAlertDialogClicked,
    )

    private fun onAlertDialogClicked() {
        dialogUiStateFlow.value = ExampleAlertDialogUiState(
            title = "Alert Dialog Title",
            onConfirm = null,
            onDismiss = null,
            onDismissRequest = { dismissDialog(dialogUiStateFlow) },
        )
    }

    private fun onCustomDialogClicked() {
        dialogUiStateFlow.value = EmptyStateDialogUiState(
            imageVector = Icons.Outlined.VisibilityOff,
            text = "Curabitur dolor sit amet adipiscing aliquet",
            subtext = "Donec sollicitudin molestie malesuada. Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            buttonText = "Show Words",
            onButtonClicked = { dismissDialog(dialogUiStateFlow) },
            actionText = "Fusce Pellentesque habitant morbi tristique senectus",
            onActionTextClicked = { dismissDialog(dialogUiStateFlow) },
        )
    }

    private fun onDialogClicked() {
        dialogUiStateFlow.value = ExampleDialogUiState(
            title = "Title of dialog",
            onConfirm = { dismissDialog(dialogUiStateFlow) },
            onDismiss = { dismissDialog(dialogUiStateFlow) },
            onDismissRequest = { dismissDialog(dialogUiStateFlow) }
        )
    }
}
