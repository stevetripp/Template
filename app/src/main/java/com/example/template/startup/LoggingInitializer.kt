package com.example.template.startup

import android.content.Context
import androidx.startup.Initializer
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.example.template.BuildConfig
import kotlin.system.measureTimeMillis

class LoggingInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        measureTimeMillis {
            Logger.setTag(BuildConfig.APPLICATION_ID)

            if (!BuildConfig.DEBUG) {
                Logger.setMinSeverity(Severity.Info)
            }
        }.let { Logger.i("""STARTUP Initializer: LoggingInitializer finished (${it}ms)""") }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf()
    }
}