package com.example.template.shared.di

import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.template.shared.model.db.AppDatabase
import com.example.template.shared.model.webservice.googleBooksSetup
import com.example.template.shared.util.file.AppFileSystem
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import org.koin.core.module.Module
import org.koin.dsl.module

actual val httpClientModule: Module = module {
    // Google Books API HttpClient
    single<HttpClient> { HttpClient(Java) { googleBooksSetup() } }
}

actual val databaseBuilderModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        Room.databaseBuilder<AppDatabase>(
            name = AppFileSystem.getDatabasesPath(AppDatabase.DATABASE_NAME).toFile().absolutePath,
        )
    }
}
