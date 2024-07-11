package com.example.template.ux.popwithresult

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun PopWithResultParentScreen(navController: NavController, viewModel: PopWithResultParentViewModel = hiltViewModel()) {
    PopWithResultParentContent(viewModel.uiState, navController::navigateUp)
    HandleNavigation(viewModelNav = viewModel, navController = navController)
    viewModel.uiState.onSetNavController(navController)
}

@Composable
private fun PopWithResultParentContent(uiState: PopWithResultParentUiState, onBack: () -> Unit = {}) {
    val resultString by uiState.resultStringFlow.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { AppTopAppBar(title = Screen.POP_WITH_RESULT.title, onBack = onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            TextButton(onClick = uiState.onClickMeClicked) {
                Text(text = "Click Me")
            }
            Text(text = "Result")
            Text(text = resultString.orEmpty())
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { PopWithResultParentContent(PopWithResultParentUiState()) }
}