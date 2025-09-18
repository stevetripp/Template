package com.example.template.ux.modalsidesheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.ModalSideSheet
import com.example.template.ux.main.Screen

@Composable
fun ModalSideSheetScreen(navController: NavController, viewModel: ModalSideSheetViewModel = hiltViewModel()) {
    ModalSideSheetContent(viewModel.uiState, onBack = navController::popBackStack)
}

@Composable
private fun ModalSideSheetContent(uiState: ModalSideSheetUiState, onBack: () -> Unit = {}) {
    val isExpanded by uiState.isExpandedFlow.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopAppBar(title = Screen.MODAL_SIDE_SHEET.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            TextButton(onClick = uiState.onExpandClicked) { Text(text = "expand") }
        }
    }

    ModalSideSheet(
        onDismissRequest = uiState.onCloseClicked,
        isExpanded = isExpanded,
    ) {
        Text(text = "Drawer content")
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { ModalSideSheetContent(ModalSideSheetUiState()) }
}