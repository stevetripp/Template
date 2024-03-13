package com.example.template.startup

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.system.measureTimeMillis

/**
 * This was added for reference only. Currently WorkManager is being initialized in [com.example.template.App]. The
 * following would need to be added to the provider section of the manifest
 *
 * <meta-data
 *  android:name="org.lds.stream.startup.WorkManagerInitializer"
 *  android:value="androidx.startup"
 * />
 */
class WorkManagerInitializer : Initializer<WorkManager> {
    override fun create(context: Context): WorkManager {
        var workManager: WorkManager

        measureTimeMillis {
            val applicationContext = checkNotNull(context.applicationContext) { "Missing Application Context" }
            val injector = EntryPoints.get(applicationContext, WorkManagerInitializerInjector::class.java)

            val configuration = Configuration.Builder()
                .setWorkerFactory(injector.getWorkerFactory())
                .build()

            WorkManager.initialize(context, configuration)

            workManager = WorkManager.getInstance(context)

        }.let { Log.i("", """"STARTUP Initializer: WorkManagerInitializer finished (${it}ms)""") }

        return workManager
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkManagerInitializerInjector {
        fun getWorkerFactory(): HiltWorkerFactory
    }
}