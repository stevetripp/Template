package com.example.template.service

import android.app.Notification
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.PlatformScheduler
import androidx.media3.exoplayer.scheduler.Scheduler
import com.example.template.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MediaDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    R.string.channel_name_resource_id,
    R.string.channel_description_resource_id
) {
    @Inject
    lateinit var mediaDownloadManager: DownloadManager

    @Inject
    lateinit var downloadNotificationHelper: DownloadNotificationHelper

    override fun getDownloadManager(): DownloadManager {
        return mediaDownloadManager
    }

    override fun getScheduler(): Scheduler {
        return PlatformScheduler(this, JOB_ID)
    }

    override fun getForegroundNotification(downloads: MutableList<Download>, notMetRequirements: Int): Notification {
        return downloadNotificationHelper.buildProgressNotification(
            this,
            R.drawable.coloring_book,
            null,
            null,
            downloads,
            notMetRequirements
        )
    }

    companion object {
        const val FOREGROUND_NOTIFICATION_ID = 1
        const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"
        const val JOB_ID = 1
    }
}