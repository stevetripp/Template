@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.example.template.churchmedia

import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download.STATE_COMPLETED
import androidx.media3.exoplayer.offline.Download.STATE_DOWNLOADING
import androidx.media3.exoplayer.offline.Download.STATE_FAILED
import androidx.media3.exoplayer.offline.Download.STATE_QUEUED
import androidx.media3.exoplayer.offline.Download.STATE_STOPPED

/**
 * From churchMedia = "1.0.3" org.lds.media.download
 */

@UnstableApi
data class DownloadProgress(
    val id: String,
    val percentDownloaded: Float,
    val state: Int
) {
    fun isQueued() = state == STATE_QUEUED
    fun isStopped() = state == STATE_STOPPED
    fun isDownloading() = state == STATE_DOWNLOADING
    fun isComplete() = state == STATE_COMPLETED
    fun isFailed() = state == STATE_FAILED

    fun isDownloadInProgress(): Boolean {
        return when {
            isQueued() || isStopped() || isDownloading() -> true
            else -> false
        }
    }

    fun isDownloadFinished(): Boolean {
        return when {
            isComplete() || isFailed() -> true
            else -> false
        }
    }
}
