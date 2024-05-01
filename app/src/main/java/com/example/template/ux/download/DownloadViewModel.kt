package com.example.template.ux.download

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor() : ViewModel() {

    private val downloadPathFlow = MutableStateFlow("")

    val uiState = DownloadUiState(
        downloadPathFlow = downloadPathFlow,
        onDownloadPathChanged = { downloadPathFlow.value = it }
    )
}