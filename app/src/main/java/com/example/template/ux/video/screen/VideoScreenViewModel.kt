package com.example.template.ux.video.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.ui.dialog.DialogUiState
import com.example.template.ui.dialog.ExampleAlertDialogUiState
import com.example.template.ui.dialog.dismissDialog
import com.example.template.ux.video.TestData
import com.example.template.ux.video.VideoItem
import com.example.template.ux.video.player.PlayerRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import javax.inject.Inject

@HiltViewModel
class VideoScreenViewModel @Inject constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val testDataFlow = MutableStateFlow(TestData.getVideos())
    private val videoItemsFlow = testDataFlow.map { it.videos }.stateInDefault(viewModelScope, emptyList())
    private val dialogUiStateFlow = MutableStateFlow<DialogUiState<*>?>(null)

    val uiState = VideoScreenUiState(
        videoItemsFlow = videoItemsFlow,
        dialogUiStateFlow = dialogUiStateFlow,
        onHlsStreamTapped = ::onHlsStreamTapped,
        onVideoRenditionTapped = ::onVideoRenditionTapped,
    )

    private fun onHlsStreamTapped(videoItem: VideoItem) {
        navigate(PlayerRoute.createRoute(videoItem.id))
    }

    private fun onVideoRenditionTapped(videoItem: VideoItem) {
        dialogUiStateFlow.value = ExampleAlertDialogUiState(
            title = "Not yet implemented. Expecting to play a downloaded video (MP4)",
            onConfirm = { dismissDialog(dialogUiStateFlow) },
            onDismiss = { dismissDialog(dialogUiStateFlow) },
            onDismissRequest = { dismissDialog(dialogUiStateFlow) }
        )
    }
}