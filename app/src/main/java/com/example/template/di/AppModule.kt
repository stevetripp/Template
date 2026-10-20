package com.example.template.di

import com.example.template.ux.notification.NotificationUtil
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.lds.media.cast.CastManager
import org.lds.mobile.util.LdsDeviceUtil

val appModule = module {
    singleOf(::NotificationUtil)
    singleOf(::LdsDeviceUtil)

    single<CastManager> { CastManager(get(), initCast = true) }
}
