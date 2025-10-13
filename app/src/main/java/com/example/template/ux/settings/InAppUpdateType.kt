package com.example.template.ux.settings

import com.google.android.play.core.install.model.AppUpdateType

/**
 * Represents the available in-app update types.
 */
enum class InAppUpdateType(val displayName: String, val appUpdateType: Int? = null) {
    NONE("None"),
    IMMEDIATE("Immediate", AppUpdateType.IMMEDIATE),
    FLEXIBLE("Flexible", AppUpdateType.FLEXIBLE)
}
