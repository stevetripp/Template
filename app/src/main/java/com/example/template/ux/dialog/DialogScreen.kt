package com.example.template.ux.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.dialog.HandleDialogUiState
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun DialogScreen(navigator: Navigation3Navigator, viewModel: DialogViewModel) {
    val uiState = viewModel.uiState
    DialogContent(uiState, navigator::pop)
    HandleDialogUiState(uiState.dialogUiStateFlow)
}

@Composable
fun DialogContent(uiState: DialogScreenUiState, onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.DIALOG.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextButton(onClick = uiState.onEmptyStateDialogClicked) {
                Text(text = "Empty State Dialog")
            }
            TextButton(onClick = uiState.onDialogClicked) {
                Text(text = "Dialog")
            }
            TextButton(onClick = uiState.onAlertDialogClicked) {
                Text(text = "Alert Dialog")
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { DialogContent(DialogScreenUiState()) }
}