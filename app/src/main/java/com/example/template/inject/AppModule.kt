package com.example.template.inject

import android.app.Application
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import com.example.template.service.MediaDownloadService
import com.example.template.churchmedia.MediaDownloadUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMedia3DatabaseProvider(application: Application): DatabaseProvider {
        return StandaloneDatabaseProvider(application)
    }

    @Provides
    @Singleton
    fun provideDownloadNotificationHelper(application: Application): DownloadNotificationHelper {
        return DownloadNotificationHelper(application, MediaDownloadService.DOWNLOAD_NOTIFICATION_CHANNEL_ID)
    }

    @Provides
    @Singleton
    fun provideMedia3DownloadCache(application: Application, mediaDatabaseProvider: DatabaseProvider): Cache {
        // A download cache should not evict media, so should use a NoopCacheEvictor.
        val downloadDirectory = application.getExternalFilesDir(null) ?: application.filesDir
        downloadDirectory.mkdirs()

        return SimpleCache(
            File(downloadDirectory, "downloads"),
            NoOpCacheEvictor(),
            mediaDatabaseProvider
        )
    }

    @Provides
    @Singleton
    fun provideMedia3DownloadManager(application: Application, databaseProvider: DatabaseProvider, downloadCache: Cache): DownloadManager {
        // Create a factory for reading the data from the network.
        val dataSourceFactory = DefaultHttpDataSource.Factory()

        // Choose an executor for downloading data. Using Runnable::run will cause each download task to
        // download data on its own thread. Passing an executor that uses multiple threads will speed up
        // download tasks that can be split into smaller parts for parallel execution. Applications that
        // already have an executor for background downloads may wish to reuse their existing executor.
        val downloadExecutor = Executor { obj: Runnable -> obj.run() }

        // Create the download manager.
        val downloadManager = DownloadManager(
            application,
            databaseProvider,
            downloadCache,
            dataSourceFactory,
            downloadExecutor
        )

        // Optionally, setters can be called to configure the download manager.
        downloadManager.maxParallelDownloads = 3

        return downloadManager
    }

    @Provides
    @Singleton
    fun provideMediaDownloadUtil(application: Application, downloadManager: DownloadManager): MediaDownloadUtil {
        return MediaDownloadUtil(application, downloadManager, MediaDownloadService::class.java)
    }
}