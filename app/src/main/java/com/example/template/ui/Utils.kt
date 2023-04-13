package com.example.template.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

object Utils {
    fun showSystemSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", context.packageName.orEmpty(), null)
        }
        context.startActivity(intent)
    }

    fun showAppNotificationsPermissions(context: Context, channelId: String? = null) {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            if (!channelId.isNullOrBlank()) {
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
            }
        } else {
            // show app info instead (not supported on less than 26)
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}