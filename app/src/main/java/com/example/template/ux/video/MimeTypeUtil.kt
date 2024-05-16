package com.example.template.ux.video

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MimeTypes
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MimeTypeUtil @Inject
constructor(
    private val application: Application
) {
    enum class MediaType constructor(val extension: String, val mimeType: String) {
        SMOOTH_STREAM(".ism", MimeTypes.APPLICATION_SS), // was "application/vnd.ms-sstr+xml"
        DASH(".mpd", MimeTypes.APPLICATION_MPD), // was "application/dash+xml"
        HLS(".m3u8", MimeTypes.APPLICATION_M3U8), // was "application/x-mpegurl"
        MP4(".mp4", MimeTypes.APPLICATION_MP4), // was "application/mp4"
        FMP4(".fmp4", MimeTypes.APPLICATION_MP4), // not found in documentation
        M4A(".m4a", MimeTypes.AUDIO_MP4), // was "video/m4a" should be AUDIO_MP4 ("audio/mp4") for .m4a
        MP3(".mp3", MimeTypes.AUDIO_MPEG), // was "audio/mp3" should be AUDIO_MPEG ("audio/mpeg") for .mp3
        TS(".ts", MimeTypes.VIDEO_MP2T), // was "video/mp2t"
        AAC(".aac", MimeTypes.AUDIO_AAC), // was "audio/aac" should be AUDIO_AAC ("audio/mp4a-latm") for .aac
        WEBM(".webm", MimeTypes.VIDEO_WEBM), // was "video/webm"
        MKV(".mkv", MimeTypes.VIDEO_MATROSKA), // was "video/mkv" should be VIDEO_MATROSKA ("video/x-matroska") for .mkv
        PNG(".png", MimeTypes.IMAGE_PNG), // was "image/png"
        JPEG(".jpeg", MimeTypes.IMAGE_JPEG), // was ""image/jpeg"
        UNKNOWN("", "")
    }

    /**
     * Determines the media type based on the mediaUri
     *
     * @param uri The uri for the media to determine the MediaType for
     * @return The resulting MediaType
     */
    fun getMimeType(uri: String): String {
        if (uri.toUri().scheme == ContentResolver.SCHEME_CONTENT) {
            return application.contentResolver?.getType(uri.toUri()).orEmpty()
        } else {

            val extension = getExtension(uri) ?: return ""

            return MediaType.entries
                .firstOrNull { it.extension == extension }
                ?.mimeType.orEmpty()
        }
    }

    private fun getExtension(mediaUri: String): String? {
        if (mediaUri.trim { it <= ' ' }.isEmpty()) {
            return null
        }

        val uri = Uri.parse(mediaUri)
        val lastPath = uri.lastPathSegment ?: return null

        val periodIndex = lastPath.lastIndexOf('.')
        if (periodIndex == -1 || periodIndex >= lastPath.length) {
            return null
        }

        val rawExtension = lastPath.substring(periodIndex)
        return rawExtension.lowercase(Locale.getDefault())
    }
}
