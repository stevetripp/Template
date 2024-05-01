package com.example.template.ux.download

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DownloadUiState(
    val downloadPathFlow: StateFlow<String> = MutableStateFlow(""),
    val onDownloadPathChanged: (String) -> Unit = {},
    val onDownload: ()->Unit = {},
)

@Composable
fun DownloadScreen(navController: NavController, viewModel: DownloadViewModel = hiltViewModel()) {
    DownloadContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun DownloadContent(uiState: DownloadUiState, onBack: () -> Unit = {}) {

    val downloadPath by uiState.downloadPathFlow.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopAppBar(title = Screen.DOWNLOAD.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            TextField(modifier = Modifier.fillMaxWidth(), value = downloadPath, onValueChange = uiState.onDownloadPathChanged)
            Button(onClick = uiState.onDownload) {
                Text(text = "Download")
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { DownloadContent(DownloadUiState()) }
}