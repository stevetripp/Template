package com.example.template.ux.download

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.exoplayer.offline.Download
import androidx.navigation.NavController
import com.example.template.churchmedia.DownloadProgress
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DownloadScreen(navController: NavController, viewModel: DownloadViewModel = hiltViewModel()) {
    DownloadContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun DownloadContent(uiState: DownloadUiState, onBack: () -> Unit = {}) {
    val downloadId by uiState.downloadIdFlow.collectAsStateWithLifecycle()
    val downloadProgress by uiState.downloadProgressFlow.collectAsStateWithLifecycle()

    Log.i("SMT", "percentDownloaded: ${downloadProgress?.percentDownloaded}")
    Scaffold(topBar = { AppTopAppBar(title = Screen.DOWNLOAD.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(modifier = Modifier.fillMaxWidth(), value = downloadId, onValueChange = uiState.onDownloadIdChanged, label = { Text(text = "Download Id") })
            if (downloadProgress?.isDownloading() == true) {
                downloadProgress?.let { CircularProgressIndicator(progress = { it.percentDownloaded }) }
            } else {
                Button(onClick = uiState.onDownload, content = { Text("Download") })
            }
            Button(onClick = uiState.onRemove, content = { Text("Remove") })
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme {
        DownloadContent(
            DownloadUiState(
                downloadProgressFlow = MutableStateFlow(DownloadProgress("", .45f, Download.STATE_DOWNLOADING)),
            )
        )
    }
}