package com.example.template.ux.download

import com.example.template.churchmedia.DownloadProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DownloadUiState(
    val downloadIdFlow: MutableStateFlow<String> = MutableStateFlow(""),
    val downloadProgressFlow: StateFlow<DownloadProgress?> = MutableStateFlow(null),
    val onDownloadIdChanged: (String) -> Unit = {},
    val onDownload: () -> Unit = {},
    val onRemove: () -> Unit = {},
)