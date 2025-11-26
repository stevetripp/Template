package com.example.template.ux.ktor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun KtorScreen(navigator: Navigation3Navigator, viewModel: KtorViewModel) {
    KtorContent(viewModel.uiState, navigator::pop)
}

@Composable
fun KtorContent(uiState: KtorUiState, onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.KTOR.title, onBack = onBack) }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Button(onClick = uiState.onExecute) { Text(text = "Execute") }
        }
    }
}