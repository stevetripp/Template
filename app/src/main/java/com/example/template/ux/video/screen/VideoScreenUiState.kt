package com.example.template.ux.video.screen

import com.example.template.ui.dialog.DialogUiState
import com.example.template.ux.video.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class VideoScreenUiState(
    val videoItemsFlow: StateFlow<List<VideoItem>> = MutableStateFlow(emptyList()),
    val dialogUiStateFlow: StateFlow<DialogUiState<*>?> = MutableStateFlow<DialogUiState<*>?>(null),
    val onHlsStreamTapped: (VideoItem) -> Unit = {},
    val onVideoRenditionTapped: (VideoItem) -> Unit = {},
)