package com.example.template.ux.parameters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar

@Composable
fun DestinationScreen(navController: NavController, viewModel: DestinationViewModel = hiltViewModel()) {
    DestinationContent(viewModel.uiState, navController::navigateUp)
}

@Composable
fun DestinationContent(uiState: DestinationUiState, onBack: () -> Unit) {
    val required by uiState.requiredFlow.collectAsStateWithLifecycle()
    val enumParameter by uiState.enumParameter.collectAsStateWithLifecycle()
    val optional by uiState.optionalFlow.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopAppBar(title = "Destination Screen", onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row {
                Text(text = "Required: ")
                Text(text = required.value)
            }
            Row {
                Text(text = "Enum Parameter: ")
                Text(text = enumParameter.toString())
            }
            Row {
                Text(text = "Optional: ")
                optional?.value?.let { Text(text = it) }
            }
        }
    }
}