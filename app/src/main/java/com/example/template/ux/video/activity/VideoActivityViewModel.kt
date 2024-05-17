package com.example.template.ux.video.activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.ux.video.CastPlayerManager
import com.example.template.ux.video.Iso3Locale
import com.example.template.ux.video.MediaPlayerItem
import com.example.template.ux.video.MimeTypeUtil
import com.example.template.ux.video.TestData
import com.example.template.ux.video.VideoId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import org.lds.mobile.util.LdsDeviceUtil
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class VideoActivityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mimeTypeUtil: MimeTypeUtil,
    val castPlayerManager: CastPlayerManager,
    deviceUtil: LdsDeviceUtil,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val iso3Lang by lazy { Iso3Locale(Locale.getDefault().isO3Language) }
    private val currentSystemLanguage: MutableStateFlow<Iso3Locale> = MutableStateFlow(iso3Lang)

    private val currentUserLanguage: Flow<Iso3Locale?> = MutableStateFlow(iso3Lang)

    val currentLanguage: StateFlow<Iso3Locale> = combine(currentSystemLanguage, currentUserLanguage) { systemLang, userLang ->
        userLang ?: systemLang
    }.stateInDefault(viewModelScope, iso3Lang)

    private val args = VideoActivityRoute.getArgs(savedStateHandle)
    val videoId = args.videoId
    val playList = args.playList

    //    val closedCaptionsStatePreferenceStateFlow: Flow<Boolean> = settingsRepository.closedCaptionsStatePreferenceFlow.flowOn(Dispatchers.IO)

    val mediaPlayerItemsFlow: Flow<List<MediaPlayerItem>?> = flow {
        emit(
            playList?.let { videoIdPlaylist ->
                videoIdPlaylist.mapNotNull { getDownloadedMediaPlayerItem(it) ?: getVodMediaPlayerItem(it) ?: getEventMediaPlayerItem(it) }
            } ?: getDownloadedMediaPlayerItem(videoId)?.let { listOf(it) } ?: getVodMediaPlayerItem(videoId)?.let { listOf(it) } ?: getEventMediaPlayerItem(videoId)?.let { listOf(it) }
        )
    }.onEach { playList ->
        playList?.find { elm -> elm.id.value == videoId.value }
    }.flowOn(Dispatchers.IO)

    val supportsPip = deviceUtil.supportsPictureInPicture() && !deviceUtil.isARC()
    val isInPip = MutableStateFlow(false)

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
            mimeTypeUtil = mimeTypeUtil,
            title = videoItem.title,
        )
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
        savedStateHandle[VideoActivityRoute.Arg.VIDEO_ID] = videoId
    }

    fun saveVideoPosition(videoId: VideoId, videoPosition: Long, duration: Long) {
//        viewModelScope.launch {
//            vodRepository.saveVideoPosition(videoId, videoPosition, duration)
//        }
    }

    fun getVideoInitialPosition(videoId: VideoId): Long {
        return 0
//        return runBlocking {
//            vodRepository.getVideoPositionById(videoId)
//        }
    }

    fun setPipState(enteredToPip: Boolean) {
        isInPip.value = enteredToPip
    }
}
