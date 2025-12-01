package com.example.template.ux.video.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.ui.dialog.DialogUiState
import com.example.template.ui.dialog.ExampleAlertDialogUiState
import com.example.template.ui.dialog.dismissDialog
import com.example.template.ux.video.TestData
import com.example.template.ux.video.VideoItem
import com.example.template.ux.video.player.PlayerActivity
import com.example.template.ux.video.player.PlayerRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl
import org.lds.mobile.ui.ext.requireActivity

@HiltViewModel
class VideoScreenViewModel @Inject constructor() : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    private val testDataFlow = MutableStateFlow(TestData.getVideos())
    private val videoItemsFlow = testDataFlow.map { it.videos }.stateInDefault(viewModelScope, emptyList())
    private val dialogUiStateFlow = MutableStateFlow<DialogUiState<*>?>(null)

    val uiState = VideoScreenUiState(
        videoItemsFlow = videoItemsFlow,
        dialogUiStateFlow = dialogUiStateFlow,
        onHlsStreamTapped = ::onHlsStreamTapped,
        onVideoRenditionTapped = ::onVideoRenditionTapped,
        onLaunchPlayer = ::onLaunchPlayer
    )

    private fun onLaunchPlayer(context: Context, videoItem: VideoItem) {
        val activity = context.requireActivity()
        PlayerActivity.launch(activity, videoItem.id)
    }

    private fun onHlsStreamTapped(videoItem: VideoItem) {
        // NOTE: Prefer calling this rather than onLaunchPlayer, but resulted in black screen when navigating back
        navigate(PlayerRoute(videoItem.id))
    }

    @Suppress("UnusedParameter")
    private fun onVideoRenditionTapped(videoItem: VideoItem) {
        dialogUiStateFlow.value = ExampleAlertDialogUiState(
            title = "Not yet implemented. Expecting to play a downloaded video (MP4)",
            onConfirm = { dismissDialog(dialogUiStateFlow) },
            onDismiss = { dismissDialog(dialogUiStateFlow) },
            onDismissRequest = { dismissDialog(dialogUiStateFlow) }
        )
    }
}