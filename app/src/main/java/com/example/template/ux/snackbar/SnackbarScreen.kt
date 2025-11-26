package com.example.template.ux.snackbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation3.navigator.Navigation3Navigator


@Composable
fun SnackbarScreen(navigator: Navigation3Navigator, viewModel: SnackbarViewModel) {
    SnackbarContent(viewModel.uiState, navigator::pop)
}


@Composable
fun SnackbarContent(uiState: SnackbarScreenUiState, onBack: () -> Unit = {}) {
    val snackbarHostState = HandleSnackbarUiState(uiState.snackBarHostUiStateFlow)
    Scaffold(
        topBar = { AppTopAppBar(title = Screen.SNACKBAR.title, onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Column(modifier = Modifier.padding(it)) {
            Button(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = uiState.onShowSnackbar
            ) {
                Text("Show Snackbar")
            }
        }
    }
}

@Preview
@Composable
private fun SnackbarContentPreview() {
    AppTheme { SnackbarContent(SnackbarScreenUiState()) }
}