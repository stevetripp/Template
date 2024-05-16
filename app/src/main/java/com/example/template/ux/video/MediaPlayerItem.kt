package com.example.template.ux.video

import android.os.Bundle
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import org.lds.media.cast.CastManager

data class MediaPlayerItem(
    val id: VideoId,
    val title: String,
    val hlsUrl: VideoUrl? = null,
    val videoRenditions: List<VideoRendition> = emptyList(),
    val artist: String? = null,
    val imageRenditions: String? = null,
    val mimeTypeUtil: MimeTypeUtil? = null,
    val downloadedMediaPlayerItem: MediaItem? = null,
    val treatAsLiveEvent: Boolean = false
) {

    fun toMediaItem(): MediaItem? {
        if (downloadedMediaPlayerItem != null) return downloadedMediaPlayerItem
        return (hlsUrl ?: videoRenditions.firstOrNull()?.url)?.let { url ->
            val extras = Bundle().apply {
                putString(IMAGE_RENDITIONS_BUNDLE_EXTRA_KEY, imageRenditions)
                putBoolean(CastManager.KEY_LIVESTREAM, treatAsLiveEvent)
            }
            MediaItem.Builder()
                .setMediaId(id.value)
                .setUri(url.value)
                .setMimeType(mimeTypeUtil?.getMimeType(url.value))
                .setMediaMetadata(MediaMetadata.Builder().setTitle(title).setExtras(extras).build())
                .build()
        }
    }

    fun toAudioMediaItem(): MediaItem? {
        return (hlsUrl ?: videoRenditions.firstOrNull()?.url)?.let { url ->
            MediaItem.Builder()
                .setMediaId(id.value)
                .setRequestMetadata(MediaItem.RequestMetadata.Builder().setMediaUri(url.value.toUri()).build())
                .setMediaMetadata(MediaMetadata.Builder().setTitle(title).setArtist(artist).build())
                .build()
        }
    }

    fun toCastAudioMediaItem(): MediaItem? {
        return videoRenditions.firstOrNull()?.url?.let { url ->
            MediaItem.Builder()
                .setMediaId(id.value)
                .setUri(url.value)
                .setMimeType(MP3_MIME_TYPE)
                .setMediaMetadata(MediaMetadata.Builder().setTitle(title).setArtist(artist).build())
                .build()
        }
    }

    companion object {
        private const val MP3_MIME_TYPE = "audio/mp3"
        const val IMAGE_RENDITIONS_BUNDLE_EXTRA_KEY = "imageRenditions"
    }

}