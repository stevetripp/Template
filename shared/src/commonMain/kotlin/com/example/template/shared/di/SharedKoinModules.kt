package com.example.template.shared.di

import com.example.template.shared.model.datastore.AppDataStore
import com.example.template.shared.model.datastore.AppPreferenceDataSource
import com.example.template.shared.model.datastore.DatastoreUtil
import com.example.template.shared.model.db.AppDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun getSharedKoinModules(): List<Module> = listOf(
    coroutineModule,
    databaseBuilderModule,
    databaseModule,
    datastoreModule,
    datastorePreferenceModule,
    httpClientModule,
    repositoryModule,
    webServiceModule,
)

// Platform-specific modules (implemented in androidMain / jvmMain)
expect val databaseBuilderModule: Module
expect val httpClientModule: Module

// ===== Database Module =====
val databaseModule = module {
    single<AppDatabase> { AppDatabase.getDatabase(get()) }
}

// ===== DataStore Module =====
val datastoreModule = module {
    single<AppDataStore> { AppDataStore(DatastoreUtil.create(DatastoreUtil.APP)) }
}

val datastorePreferenceModule = module {
    singleOf(::AppPreferenceDataSource)
}
