package com.example.template.ux.video.player

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.navigation.toRoute
import com.example.template.ux.video.TestData
import com.example.template.ux.video.VideoId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.lds.media.cast.CastManager
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import org.lds.mobile.util.LdsDeviceUtil
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    deviceUtil: LdsDeviceUtil,
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {

    private val playerRoute = savedStateHandle.toRoute<PlayerRoute>(PlayerRoute.typeMap())
    private val videoId = playerRoute.videoId

    val mediaItemsFlow: Flow<PlayList> = flow {
        val playlist = PlayList(videoId, getMediaItems())
        emit(playlist)
    }

    val supportsPip = deviceUtil.supportsPictureInPicture() && !deviceUtil.isARC()

    private fun getMediaItems(): List<MediaItem> {
        val videoItems = TestData.getVideos().videos
        return videoItems.map {
            val extras = Bundle().apply {
                putString("imageRenditions", it.imageRenditions)
                putBoolean(CastManager.KEY_LIVESTREAM, false)
            }
            MediaItem.Builder()
                .setMediaId(it.id.value) // Sets the optional media ID which identifies the media item.
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.title) // Added but don't see anywhere
                        .setExtras(extras) // May be need for casting
                        .build()
                )
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .setUri(it.hlsUrl.value)
                .build()
        }
    }
}

data class PlayList(val videoId: VideoId, val mediaItems: List<MediaItem>)