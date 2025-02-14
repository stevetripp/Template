package com.example.template.startup

import android.content.Context
import androidx.startup.Initializer
import co.touchlab.kermit.Logger
import com.example.template.ux.notification.NotificationUtil
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.system.measureTimeMillis

class NotificationInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        measureTimeMillis {
            val applicationContext = requireNotNull(context.applicationContext) { "Missing Application Context" }
            val injector = EntryPoints.get(applicationContext, Injector::class.java)
            val notificationUtil = injector.getNotificationUtil()
            notificationUtil.initialize()
        }.let { Logger.i("""STARTUP Initializer: NotificationInitializer finished (${it}ms)""") }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface Injector {
        fun getNotificationUtil(): NotificationUtil
    }
}