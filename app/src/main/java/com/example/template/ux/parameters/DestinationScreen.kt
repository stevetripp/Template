package com.example.template.ux.parameters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar

@Composable
fun DestinationScreen(navController: NavController, viewModel: DestinationViewModel = hiltViewModel()) {
    DestinationContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun DestinationContent(uiState: DestinationUiState, onBack: () -> Unit) {
    val required by uiState.requiredFlow.collectAsStateWithLifecycle()
    val optional by uiState.optionalFlow.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopAppBar(title = "Destination Screen", onBack = onBack) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(text = required)
            optional?.let { Text(text = it) }
        }
    }
}