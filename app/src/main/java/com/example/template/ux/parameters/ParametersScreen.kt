package com.example.template.ux.parameters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import org.lds.mobile.ui.compose.navigation.HandleNavigation
import com.example.template.ux.main.Screen

@Composable
fun ParametersScreen(navController: NavController, viewModel: ParametersViewModel = hiltViewModel()) {
    ParametersContent(viewModel.uiState, navController::popBackStack)
    HandleNavigation(viewModel, navController)
}

@Composable
fun ParametersContent(uiState: ParametersUiState, onBack: () -> Unit) {
    val requiredValue by uiState.requiredValueFlow.collectAsStateWithLifecycle()
    val optionalValue by uiState.optionalValueFlow.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopAppBar(title = Screen.PARAMETERS.title, onBack = onBack) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(value = requiredValue, onValueChange = uiState.onRequiredValueChanged)
            TextField(value = optionalValue.orEmpty(), onValueChange = uiState.onOptionalValueChanged)
            TextButton(onClick = uiState.onButtonClick) { Text(text = "Tap Me") }
        }
    }
}