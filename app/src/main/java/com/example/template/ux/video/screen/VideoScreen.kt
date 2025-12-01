package com.example.template.ux.video.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.dialog.HandleDialogUiState
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation3.navigator.Navigation3Navigator
import org.lds.mobile.ui.compose.navigation.HandleNavigation3

@Composable
fun VideoScreen(navigator: Navigation3Navigator, viewModel: VideoScreenViewModel) {
    VideoContent(viewModel.uiState, navigator::pop)
    HandleNavigation3(viewModelNavigation = viewModel, navigator = navigator)
    HandleDialogUiState(viewModel.uiState.dialogUiStateFlow)
}

@Composable
fun VideoContent(uiState: VideoScreenUiState, onBack: () -> Unit = {}) {

    val videoItems by uiState.videoItemsFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(topBar = { AppTopAppBar(title = Screen.VIDEO.title, onBack = onBack) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item { Text(text = "HLS Streams", fontWeight = FontWeight.Bold) }
            items(videoItems) {
                TextButton(onClick = { uiState.onLaunchPlayer(context, it) }) { Text(text = it.hlsUrl.value, maxLines = 1) }
            }
            item { Text(text = "Video Renditions", fontWeight = FontWeight.Bold) }
            items(videoItems) {
                TextButton(onClick = { uiState.onVideoRenditionTapped(it) }) { Text(text = it.videoRenditions.first().url.value, maxLines = 1) }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { VideoContent(VideoScreenUiState()) }
}