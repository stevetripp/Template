package com.example.template.ux.snackbar

import android.annotation.SuppressLint
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("ComposableNaming")
@Composable
fun HandleSnackbarUiState(snackbarUiStateFlow: StateFlow<SnackbarUiState?>): SnackbarHostState {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarUiState by snackbarUiStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarUiState) {
        val (message, label, duration) = snackbarUiState ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = message,
            actionLabel = label,
            duration = duration,
        )

        if (result == SnackbarResult.ActionPerformed) snackbarUiState?.onAction?.invoke()
        snackbarUiState?.resetSnackbarUiState?.invoke()
    }

    return snackbarHostState
}

data class SnackbarUiState(
    val message: String,
    val actionLabel: String,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onAction: () -> Unit,
    val resetSnackbarUiState: () -> Unit = {},
)
