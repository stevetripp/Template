@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.example.template.churchmedia

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * From churchMedia = "1.0.3" org.lds.media.download
 */

class MediaDownloadUtil
    (
    private val context: Context,
    private val downloadManager: DownloadManager,
    private val downloadServiceClass: Class<out DownloadService>
) {
    fun addDownload(id: String, url: String) {
        val downloadRequest: DownloadRequest = DownloadRequest.Builder(id, url.toUri()).build()
        DownloadService.sendAddDownload(
            context,
            downloadServiceClass,
            downloadRequest,
            false
        )
    }

    fun removeDownload(id: String) {
        DownloadService.sendRemoveDownload(
            context,
            downloadServiceClass,
            id,
            false
        )
    }

    fun stopDownload(id: String) {
        DownloadService.sendSetStopReason(
            context,
            downloadServiceClass,
            id,
            Download.STOP_REASON_NONE,
            false
        )
    }

    fun pauseDownloads() {
        DownloadService.sendPauseDownloads(
            context,
            downloadServiceClass,
            false
        )
    }

    fun resumeDownloads() {
        DownloadService.sendResumeDownloads(
            context,
            downloadServiceClass,
            false
        )
    }

    fun getDownload(id: String): Download? {
        return downloadManager.downloadIndex.getDownload(id)
    }

    private fun getDownloadProgress(id: String): DownloadProgress? {
        val download = getDownload(id) ?: return null

        return DownloadProgress(
            id = id,
            download.percentDownloaded / 100, // convert to 0.0 - 1.0 value (compatibility with Progress Indicator)
            download.state
        )
    }

    fun getDownloadProgressFlow(
        id: String,
        initialDelay: Long = DEFAULT_POLL_INITIAL_DELAY_MS,
        pollInterval: Long = DEFAULT_POLL_INTERVAL_MS
    ): Flow<DownloadProgress> = flow {
        delay(initialDelay)

        var downloadProgress = getDownloadProgress(id) ?: return@flow
        emit(downloadProgress)

        while (downloadProgress.isDownloadFinished().not()) {
            delay(pollInterval)

            downloadProgress = getDownloadProgress(id) ?: return@flow
            emit(downloadProgress)
        }
    }

    fun getDownloadProgressForeverFlow(
        id: String,
        initialDelay: Long = DEFAULT_POLL_INITIAL_DELAY_MS,
        pollInterval: Long = DEFAULT_FOREVER_POLL_INTERVAL_MS
    ): Flow<DownloadProgress?> = flow {
        delay(initialDelay)

        var downloadProgress = getDownloadProgress(id)
        emit(downloadProgress)

        while (true) {
            delay(pollInterval)

            downloadProgress = getDownloadProgress(id)
            emit(downloadProgress)
        }
    }

    companion object {
        const val DEFAULT_POLL_INITIAL_DELAY_MS: Long = 0
        const val DEFAULT_POLL_INTERVAL_MS: Long = 100
        const val DEFAULT_FOREVER_POLL_INTERVAL_MS: Long = 1000
    }
}
