package com.example.template.ux.snackbar

import android.app.Application
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SnackbarViewModel @Inject constructor(
    private val application: Application,
) : ViewModel() {

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
            onAction = { Toast.makeText(application, "Action clicked!", Toast.LENGTH_SHORT).show() },
            resetSnackbarUiState = { snackbarHostUiStateFlow.value = null }
        )
    }
}