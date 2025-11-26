package com.example.template.inject

import android.app.Application
import android.content.Context
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import org.lds.media.cast.CastManager

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideCastManager(@ApplicationContext context: Context): CastManager = CastManager(context, initCast = true)

    @Provides
    @Singleton
    fun provideMedia3DatabaseProvider(application: Application): DatabaseProvider {
        return StandaloneDatabaseProvider(application)
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
}