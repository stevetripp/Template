package com.example.template.ux.video

import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import org.lds.mobile.util.LdsDeviceUtil
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
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

    val nextTrack = MutableStateFlow<NextTrack?>(null)
    private val showUpcomingLabel = MutableStateFlow(true)
    private val upcomingTrackFocusState = MutableStateFlow(false)
    val isUpcomingTrackVisible = upcomingTrackFocusState.mapLatest {
        if (it) View.VISIBLE else View.INVISIBLE
    }.stateInDefault(viewModelScope, View.INVISIBLE)
    val shouldShowUpcomingLabel = combine(nextTrack, showUpcomingLabel) { next, show ->
        next?.let {
            if (next.secondsUntil <= 10 && show) View.VISIBLE else View.GONE
        } ?: View.GONE
    }.stateInDefault(viewModelScope, View.GONE)

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

    @Suppress("MaxLineLength")
    private suspend fun getVodMediaPlayerItem(id: VideoId): MediaPlayerItem? {
        return MediaPlayerItem(
            id = VideoId("40be9732f8dc11ee9d4eeeeeac1ea7e4ccac256f"),
            imageRenditions = "60x34,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/60%2C/0/default 100x56,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/100%2C/0/default 200x113,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/200%2C/0/default 250x141,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/250%2C/0/default 320x180,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/320%2C/0/default 500x281,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/500%2C/0/default 640x360,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/640%2C/0/default 800x450,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/800%2C/0/default 1280x720,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/1280%2C/0/default 1600x900,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/1600%2C/0/default 1920x1080,https://www.churchofjesuschrist.org/imgs/d94f51d1bd9bbf9954dfe0645e6a67c923c1430c/full/1920%2C/0/default",
            hlsUrl = VideoUrl("https://mediasrv.churchofjesuschrist.org/media-services/GA/type/6350783709112/hls.m3u8"),
//                videoRenditions = it.videoRenditions,
//                mimeTypeUtil = mimeTypeUtil,
            title = "The Influence of Women",
        )
    }


    fun updateNextTrack(value: NextTrack?) {
        nextTrack.value = value
    }

    fun updateShowUpcomingLabel(value: Boolean) {
        showUpcomingLabel.value = value
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

    fun updateUpcomingTrackFocusState(focused: Boolean) {
        upcomingTrackFocusState.value = focused
    }

    fun setPipState(enteredToPip: Boolean) {
        isInPip.value = enteredToPip
    }
}
