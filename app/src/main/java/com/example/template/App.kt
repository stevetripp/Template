package com.example.template

import android.app.Application
import com.example.template.di.getAllKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class App : Application(), KoinStartup {
    // Startup created to support Android Initializers (which must come BEFORE onCreate())
    // https://insert-koin.io/docs/reference/koin-android/start/#start-koin-with-androidx-startup-401-experimental
    override fun onKoinStartup() = koinConfiguration {

        androidContext(this@App)
        androidLogger(Level.INFO)

        modules(getAllKoinModules())

//        workManagerFactory()
    }
}
