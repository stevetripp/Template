package com.example.template.ux.snackbar

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SnackbarViewModel @Inject constructor() : ViewModel() {

    private val snackbarHostUiStateFlow = MutableStateFlow<SnackbarUiState?>(null)

    val uiState = SnackbarScreenUiState(
        snackBarHostUiStateFlow = snackbarHostUiStateFlow,
        onShowSnackbar = ::onShowSnackbar
    )

    private fun onShowSnackbar() {
        snackbarHostUiStateFlow.value = SnackbarUiState(
            message = "The snackbar message",
            actionLabel = "Action",
            duration = SnackbarDuration.Short,
            onAction = { Log.i("SMT", "Action clicked") },
            resetSnackbarUiState = { snackbarHostUiStateFlow.value = null }
        )
    }
}