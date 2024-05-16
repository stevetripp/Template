package org.lds.media.cast

import android.net.Uri
import androidx.media3.cast.MediaItemConverter
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.DrmConfiguration
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.Assertions
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.common.images.WebImage
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

/**
 * This class will set the livestream type that is required to be set for the Church's Chromecast Receiver
 * during the conversion of a Media3 MediaItem -> MediaQueueItem from the Cast sdk (needed for casting).
 *
 * This file is a modified version of the DefaultMediaItemConverter.java in the Media3 cast library with the modification
 * of retrieving an extra from the MediaItem metadata used to determine if it is a live stream (set by our apps).
 */
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Suppress("TooGenericExceptionThrown")
class ChurchCastMediaItemConverter : MediaItemConverter {
    override fun toMediaItem(mediaQueueItem: MediaQueueItem): MediaItem {
        val mediaInfo = mediaQueueItem.media
        Assertions.checkNotNull(mediaInfo)
        val metadataBuilder = MediaMetadata.Builder()
        val metadata = mediaInfo?.metadata
        if (metadata != null) {
            if (metadata.containsKey(com.google.android.gms.cast.MediaMetadata.KEY_TITLE)) {
                metadataBuilder.setTitle(metadata.getString(com.google.android.gms.cast.MediaMetadata.KEY_TITLE))
            }
            if (metadata.containsKey(com.google.android.gms.cast.MediaMetadata.KEY_SUBTITLE)) {
                metadataBuilder.setSubtitle(metadata.getString(com.google.android.gms.cast.MediaMetadata.KEY_SUBTITLE))
            }
            if (metadata.containsKey(com.google.android.gms.cast.MediaMetadata.KEY_ARTIST)) {
                metadataBuilder.setArtist(metadata.getString(com.google.android.gms.cast.MediaMetadata.KEY_ARTIST))
            }
            if (metadata.containsKey(com.google.android.gms.cast.MediaMetadata.KEY_ALBUM_ARTIST)) {
                metadataBuilder.setAlbumArtist(metadata.getString(com.google.android.gms.cast.MediaMetadata.KEY_ALBUM_ARTIST))
            }
            if (metadata.containsKey(com.google.android.gms.cast.MediaMetadata.KEY_ALBUM_TITLE)) {
                metadataBuilder.setArtist(metadata.getString(com.google.android.gms.cast.MediaMetadata.KEY_ALBUM_TITLE))
            }
            if (metadata.images.isNotEmpty()) {
                metadataBuilder.setArtworkUri(metadata.images[0].url)
            }
            if (metadata.containsKey(com.google.android.gms.cast.MediaMetadata.KEY_COMPOSER)) {
                metadataBuilder.setComposer(metadata.getString(com.google.android.gms.cast.MediaMetadata.KEY_COMPOSER))
            }
            if (metadata.containsKey(com.google.android.gms.cast.MediaMetadata.KEY_DISC_NUMBER)) {
                metadataBuilder.setDiscNumber(metadata.getInt(com.google.android.gms.cast.MediaMetadata.KEY_DISC_NUMBER))
            }
            if (metadata.containsKey(com.google.android.gms.cast.MediaMetadata.KEY_TRACK_NUMBER)) {
                metadataBuilder.setTrackNumber(metadata.getInt(com.google.android.gms.cast.MediaMetadata.KEY_TRACK_NUMBER))
            }
        }
        // `mediaQueueItem` came from `toMediaQueueItem()` so the custom JSON data must be set.
        return getMediaItem(
            Assertions.checkNotNull(mediaInfo?.customData), metadataBuilder.build()
        )
    }

    override fun toMediaQueueItem(mediaItem: MediaItem): MediaQueueItem {
        Assertions.checkNotNull(mediaItem.localConfiguration)
        requireNotNull(mediaItem.localConfiguration?.mimeType) { "The item must specify its mimeType" }
        val metadata = com.google.android.gms.cast.MediaMetadata(
            if (MimeTypes.isAudio(mediaItem.localConfiguration?.mimeType))
                com.google.android.gms.cast.MediaMetadata.MEDIA_TYPE_MUSIC_TRACK else com.google.android.gms.cast.MediaMetadata.MEDIA_TYPE_MOVIE
        )
        if (mediaItem.mediaMetadata.title != null) {
            metadata.putString(com.google.android.gms.cast.MediaMetadata.KEY_TITLE, mediaItem.mediaMetadata.title.toString())
        }
        if (mediaItem.mediaMetadata.subtitle != null) {
            metadata.putString(com.google.android.gms.cast.MediaMetadata.KEY_SUBTITLE, mediaItem.mediaMetadata.subtitle.toString())
        }
        if (mediaItem.mediaMetadata.artist != null) {
            metadata.putString(com.google.android.gms.cast.MediaMetadata.KEY_ARTIST, mediaItem.mediaMetadata.artist.toString())
        }
        if (mediaItem.mediaMetadata.albumArtist != null) {
            metadata.putString(
                com.google.android.gms.cast.MediaMetadata.KEY_ALBUM_ARTIST, mediaItem.mediaMetadata.albumArtist.toString()
            )
        }
        if (mediaItem.mediaMetadata.albumTitle != null) {
            metadata.putString(
                com.google.android.gms.cast.MediaMetadata.KEY_ALBUM_TITLE, mediaItem.mediaMetadata.albumTitle.toString()
            )
        }
        mediaItem.mediaMetadata.artworkUri?.let { artworkUri ->
            metadata.addImage(WebImage(artworkUri))
        }
        if (mediaItem.mediaMetadata.composer != null) {
            metadata.putString(com.google.android.gms.cast.MediaMetadata.KEY_COMPOSER, mediaItem.mediaMetadata.composer.toString())
        }
        mediaItem.mediaMetadata.discNumber?.let { discNumber ->
            metadata.putInt(com.google.android.gms.cast.MediaMetadata.KEY_DISC_NUMBER, discNumber)
        }
        mediaItem.mediaMetadata.trackNumber?.let { trackNumber ->
            metadata.putInt(com.google.android.gms.cast.MediaMetadata.KEY_TRACK_NUMBER, trackNumber)
        }
        val contentUrl = mediaItem.localConfiguration?.uri.toString()
        val contentId = if (mediaItem.mediaId == MediaItem.DEFAULT_MEDIA_ID) contentUrl else mediaItem.mediaId
        val mediaInfo = MediaInfo.Builder(contentId)
            .setStreamType(getStreamTypeFromMediaItem(mediaItem)) // determine if our apps set this to be a livestream
            .setContentType(mediaItem.localConfiguration?.mimeType)
            .setContentUrl(contentUrl)
            .setMetadata(metadata)
            .setCustomData(getCustomData(mediaItem))
            .build()
        return MediaQueueItem.Builder(mediaInfo).build()
    }

    /**
     * This will check for the MediaItem metadata extra that our apps need to set in Media3 to identify this item
     * as a livestream to set the flag needed by the Church's chromecast receiver.
     */
    private fun getStreamTypeFromMediaItem(mediaItem: MediaItem): Int {
        val isLivestream = mediaItem.mediaMetadata.extras?.getBoolean(CastManager.KEY_LIVESTREAM) ?: false
        return if (isLivestream) MediaInfo.STREAM_TYPE_LIVE else MediaInfo.STREAM_TYPE_BUFFERED
    }

    companion object {
        private const val KEY_MEDIA_ITEM = "mediaItem"
        private const val KEY_PLAYER_CONFIG = "exoPlayerConfig"
        private const val KEY_MEDIA_ID = "mediaId"
        private const val KEY_URI = "uri"
        private const val KEY_TITLE = "title"
        private const val KEY_MIME_TYPE = "mimeType"
        private const val KEY_DRM_CONFIGURATION = "drmConfiguration"
        private const val KEY_UUID = "uuid"
        private const val KEY_LICENSE_URI = "licenseUri"
        private const val KEY_REQUEST_HEADERS = "requestHeaders"

        // Deserialization.
        private fun getMediaItem(
            customData: JSONObject, mediaMetadata: MediaMetadata
        ): MediaItem {
            return try {
                val mediaItemJson = customData.getJSONObject(KEY_MEDIA_ITEM)
                val builder = MediaItem.Builder()
                    .setUri(Uri.parse(mediaItemJson.getString(KEY_URI)))
                    .setMediaId(mediaItemJson.getString(KEY_MEDIA_ID))
                    .setMediaMetadata(mediaMetadata)
                if (mediaItemJson.has(KEY_MIME_TYPE)) {
                    builder.setMimeType(mediaItemJson.getString(KEY_MIME_TYPE))
                }
                if (mediaItemJson.has(KEY_DRM_CONFIGURATION)) {
                    populateDrmConfiguration(mediaItemJson.getJSONObject(KEY_DRM_CONFIGURATION), builder)
                }
                builder.build()
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
        }

        @Throws(JSONException::class)
        private fun populateDrmConfiguration(json: JSONObject, mediaItem: MediaItem.Builder) {
            val drmConfiguration = DrmConfiguration.Builder(UUID.fromString(json.getString(KEY_UUID)))
                .setLicenseUri(json.getString(KEY_LICENSE_URI))
            val requestHeadersJson = json.getJSONObject(KEY_REQUEST_HEADERS)
            val requestHeaders = HashMap<String, String>()
            val iterator = requestHeadersJson.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                requestHeaders[key] = requestHeadersJson.getString(key)
            }
            drmConfiguration.setLicenseRequestHeaders(requestHeaders)
            mediaItem.setDrmConfiguration(drmConfiguration.build())
        }

        // Serialization.
        private fun getCustomData(mediaItem: MediaItem): JSONObject {
            val json = JSONObject()
            try {
                json.put(KEY_MEDIA_ITEM, getMediaItemJson(mediaItem))
                val playerConfigJson = getPlayerConfigJson(mediaItem)
                if (playerConfigJson != null) {
                    json.put(KEY_PLAYER_CONFIG, playerConfigJson)
                }
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
            return json
        }

        @Throws(JSONException::class)
        private fun getMediaItemJson(mediaItem: MediaItem): JSONObject {
            Assertions.checkNotNull(mediaItem.localConfiguration)
            val json = JSONObject()
            json.put(KEY_MEDIA_ID, mediaItem.mediaId)
            json.put(KEY_TITLE, mediaItem.mediaMetadata.title)
            json.put(KEY_URI, mediaItem.localConfiguration?.uri.toString())
            json.put(KEY_MIME_TYPE, mediaItem.localConfiguration?.mimeType)
            if (mediaItem.localConfiguration?.drmConfiguration != null) {
                json.put(
                    KEY_DRM_CONFIGURATION,
                    getDrmConfigurationJson(mediaItem.localConfiguration?.drmConfiguration)
                )
            }
            return json
        }

        @Throws(JSONException::class)
        private fun getDrmConfigurationJson(drmConfiguration: DrmConfiguration?): JSONObject {
            val json = JSONObject()
            json.put(KEY_UUID, drmConfiguration?.scheme)
            json.put(KEY_LICENSE_URI, drmConfiguration?.licenseUri)
            json.put(KEY_REQUEST_HEADERS, JSONObject(drmConfiguration?.licenseRequestHeaders as Map<*, *>?))
            return json
        }

        @Throws(JSONException::class)
        private fun getPlayerConfigJson(mediaItem: MediaItem): JSONObject? {
            if (mediaItem.localConfiguration == null
                || mediaItem.localConfiguration?.drmConfiguration == null
            ) {
                return null
            }
            val drmConfiguration = mediaItem.localConfiguration?.drmConfiguration
            val drmScheme: String = if (C.WIDEVINE_UUID == drmConfiguration?.scheme) {
                "widevine"
            } else if (C.PLAYREADY_UUID == drmConfiguration?.scheme) {
                "playready"
            } else {
                return null
            }
            val playerConfigJson = JSONObject()
            playerConfigJson.put("withCredentials", false)
            playerConfigJson.put("protectionSystem", drmScheme)
            if (drmConfiguration.licenseUri != null) {
                playerConfigJson.put("licenseUrl", drmConfiguration.licenseUri)
            }
            if (!drmConfiguration.licenseRequestHeaders.isEmpty()) {
                playerConfigJson.put("headers", JSONObject(drmConfiguration.licenseRequestHeaders as Map<*, *>?))
            }
            return playerConfigJson
        }
    }
}
