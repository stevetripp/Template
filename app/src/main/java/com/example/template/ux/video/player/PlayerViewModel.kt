package com.example.template.ux.video.player

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.template.ux.video.CastPlayerManager
import com.example.template.ux.video.MediaPlayerItem
import com.example.template.ux.video.TestData
import com.example.template.ux.video.VideoId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.lds.media.cast.CastManager
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import org.lds.mobile.util.LdsDeviceUtil
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
//    private val mimeTypeUtil: MimeTypeUtil,
    val castPlayerManager: CastPlayerManager,
    deviceUtil: LdsDeviceUtil,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val args = PlayerRoute.getArgs(savedStateHandle)
    private val videoId = args.videoId
    private val playList = args.playList

    val mediaItemsFlow: Flow<PlayList> = flow {
        val playlist = PlayList(videoId, getMediaItems())
        emit(playlist)
    }

    val supportsPip = deviceUtil.supportsPictureInPicture() && !deviceUtil.isARC()
//    val isInPip = MutableStateFlow(false)

    private suspend fun getDownloadedMediaPlayerItem(id: VideoId): MediaPlayerItem? {
        return null
//        return downloadRepository.getLocalVideo(id)?.let {
//            val downloadRequest = mediaDownloadUtil.getDownload(it.id.value)?.request ?: return@let null
//            val extras = Bundle().apply {
//                putString(IMAGE_RENDITIONS_BUNDLE_EXTRA_KEY, it.imageRenditions)
//            }
//            MediaPlayerItem(
//                id = it.id,
//                title = it.title.orEmpty(),
//                downloadedMediaPlayerItem = MediaItem.Builder()
//                    .setMediaMetadata(MediaMetadata.Builder().setTitle(it.title).setExtras(extras).build())
//                    .setMediaId(downloadRequest.id)
//                    .setUri(downloadRequest.uri)
//                    .setCustomCacheKey(downloadRequest.customCacheKey)
//                    .setMimeType(mimeTypeUtil.getMimeType(downloadRequest.uri.toString()))
//                    .setStreamKeys(downloadRequest.streamKeys)
//                    .build()
//            )
//        }
    }

    private fun getVodMediaPlayerItem(id: VideoId): MediaPlayerItem? {
        val videoItem = TestData.getVideos().videos.find { it.id == id } ?: return null
        return MediaPlayerItem(
            id = videoItem.id,
            imageRenditions = videoItem.imageRenditions,
            hlsUrl = videoItem.hlsUrl,
            videoRenditions = videoItem.videoRenditions,
            title = videoItem.title,
        )
    }

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
                .setUri(it.hlsUrl.value)
                .build()
        }
    }

    private suspend fun getEventMediaPlayerItem(id: VideoId): MediaPlayerItem? {
        return null
//        return streamRepository.getEvent(id)?.let {
//            it.url ?: return@let null
//            val treatAsLiveEvent = it.isLive() || it.url.value.contains("psdcdn.churchofjesuschrist.org")
//            if (it.isLive()) cancelUpcomingEventNotificationWorkers()
//            MediaPlayerItem(
//                id = it.id,
//                imageRenditions = it.imageRenditions,
//                hlsUrl = it.url,
//                mimeTypeUtil = mimeTypeUtil,
//                title = it.title.orEmpty(),
//                treatAsLiveEvent = treatAsLiveEvent,
//            )
//        }
    }

    fun setVideoId(videoId: String?) {
        savedStateHandle[PlayerRoute.Arg.VIDEO_ID] = videoId
    }

    fun setPipState(enteredToPip: Boolean) {
//        isInPip.value = enteredToPip
    }
}

data class PlayList(val videoId: VideoId, val mediaItems: List<MediaItem>)