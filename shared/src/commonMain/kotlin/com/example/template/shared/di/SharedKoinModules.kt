package com.example.template.shared.di

import com.example.template.shared.model.datastore.AppDataStore
import com.example.template.shared.model.datastore.AppPreferenceDataSource
import com.example.template.shared.model.datastore.DatastoreUtil
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun getSharedKoinModules(): List<Module> = listOf(
    coroutineModule,
    datastoreModule,
    datastorePreferenceModule,
    httpClientModule,
    webServiceModule,
)

// Platform-specific modules (implemented in androidMain / jvmMain)
expect val httpClientModule: Module

// ===== DataStore Module =====
val datastoreModule = module {
    single<AppDataStore> { AppDataStore(DatastoreUtil.create(DatastoreUtil.APP)) }
}

val datastorePreferenceModule = module {
    singleOf(::AppPreferenceDataSource)
}
