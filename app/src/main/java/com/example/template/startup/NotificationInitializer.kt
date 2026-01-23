package com.example.template.startup

import android.content.Context
import androidx.startup.Initializer
import co.touchlab.kermit.Logger
import com.example.template.ux.notification.NotificationUtil
import kotlin.system.measureTimeMillis
import org.koin.androix.startup.KoinInitializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationInitializer : Initializer<Unit>, KoinComponent {
    private val notificationUtil: NotificationUtil by inject()

    override fun create(context: Context) {
        measureTimeMillis {
            notificationUtil.initialize()
        }.let { Logger.i("""STARTUP Initializer: NotificationInitializer finished (${it}ms)""") }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(KoinInitializer::class.java) // needed for injection used by NotificationUtil
    }
}