package com.example.template.ux.ktor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ux.main.Screen

@Composable
fun KtorScreen(navController: NavController, viewModel: KtorViewModel = hiltViewModel()) {
    KtorContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun KtorContent(uiState: KtorUiState, onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.KTOR.title, onBack = onBack) }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)) {
            Button(onClick = uiState.onExecute) { Text(text = "Execute") }
        }
    }
}