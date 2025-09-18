package com.example.template.ux.urinavigation

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
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun UriNavigationScreen(navController: NavController, viewModel: UriNavigationViewModel = hiltViewModel()) {
    UriNavigationContent(viewModel.uiState, navController::popBackStack)
    HandleNavigation(viewModel, navController)
}

@Composable
fun UriNavigationContent(uiState: UriNavigationUiState, onBack: () -> Unit) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.URI_NAVIGATION.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Button(onClick = uiState.onDestination) { Text(text = "Destination") }
            Button(onClick = uiState.onFlippable) { Text(text = "Flippable") }
            Button(onClick = uiState.onPullRefresh) { Text(text = "Pull Refresh") }
        }
    }
}