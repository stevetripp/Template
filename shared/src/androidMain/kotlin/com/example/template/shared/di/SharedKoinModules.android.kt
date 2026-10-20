package com.example.template.shared.di

import android.app.Application
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.template.shared.model.db.AppDatabase
import com.example.template.shared.model.webservice.googleBooksSetup
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual val httpClientModule = module {
    // Google Books API HttpClient
    single<HttpClient> { HttpClient(OkHttp.create()) { googleBooksSetup() } }
}

actual val databaseBuilderModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val application: Application = androidApplication()
        Room.databaseBuilder<AppDatabase>(
            context = application,
            name = application.getDatabasePath(AppDatabase.DATABASE_NAME).absolutePath,
        )
    }
}
