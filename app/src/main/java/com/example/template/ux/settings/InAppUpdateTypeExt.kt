package com.example.template.ux.settings

import com.example.template.shared.model.settings.InAppUpdateType
import com.google.android.play.core.install.model.AppUpdateType

/**
 * Maps the platform-neutral [InAppUpdateType] to the Play Core [AppUpdateType], or null for [InAppUpdateType.NONE].
 */
val InAppUpdateType.appUpdateType: Int?
    get() = when (this) {
        InAppUpdateType.NONE -> null
        InAppUpdateType.IMMEDIATE -> AppUpdateType.IMMEDIATE
        InAppUpdateType.FLEXIBLE -> AppUpdateType.FLEXIBLE
    }
