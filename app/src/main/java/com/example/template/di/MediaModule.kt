package com.example.template.di

import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val mediaModule = module {
    single<DatabaseProvider> { StandaloneDatabaseProvider(androidApplication()) }

    single<Cache> {
        val downloadDirectory = androidApplication().getExternalFilesDir(null) ?: androidApplication().filesDir
        downloadDirectory.mkdirs()
        SimpleCache(File(downloadDirectory, "downloads"), NoOpCacheEvictor(), get<DatabaseProvider>())
    }
}
