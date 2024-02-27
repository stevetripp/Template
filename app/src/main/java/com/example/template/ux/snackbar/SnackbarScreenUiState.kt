package com.example.template.ux.snackbar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SnackbarScreenUiState(
    val snackBarHostUiStateFlow: StateFlow<SnackbarUiState?> = MutableStateFlow(null),
    val onShowSnackbar: () -> Unit = {},
)