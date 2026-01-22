package com.example.template.ux.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.template.R
import com.example.template.util.SmtLogger
import com.example.template.ux.main.MainActivity
import com.example.template.ux.pullrefresh.PullRefreshRoute
import com.example.template.ux.pullrefresh.deepLinkPatterns

class NotificationUtil(application: Application) {
    private val notificationManager: NotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun initialize() {
        SmtLogger.i("""initialize""")
        val importance = NotificationManager.IMPORTANCE_DEFAULT // Notification importance

        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }

        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(context: Context) {
        val notificationId = 1 // Unique ID for your notification

        // Create an explicit intent for an Activity in your app.
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.cast_ic_notification_small_icon) // Required: Small icon (white on transparent)
            .setContentTitle("Template Notification Title")
            .setContentText("Template notification message.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Notification priority
            .setContentIntent(pendingIntent) // add the pending intent here
            .setAutoCancel(true) // Remove the notification when the user clicks it.

        notificationManager.notify(notificationId, builder.build())
    }

    fun showDeepLinkNotification(context: Context) {
        val notificationId = 2 // A unique ID for this notification

        // Create the deep link URI by extracting the custom URL from the pattern
        val deepLinkUri = PullRefreshRoute.deepLinkPatterns.last().url.toString().toUri()

        // Create an intent with the deep link URI
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            setPackage(context.packageName) // To prevent other apps from handling
        }

        // Wrap the intent in a PendingIntent
        val deepLinkPendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // Build the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.cast_ic_notification_small_icon) // Required: Small icon (white on transparent)
            .setContentTitle("Template Deep Link Notification")
            .setContentText("Template Deep Link notification message")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(deepLinkPendingIntent) // Set the deep link PendingIntent
            .setAutoCancel(true)

        // Show the notification
        notificationManager.notify(notificationId, builder.build())
    }

    companion object {
        private const val CHANNEL_ID = "template_channel_id" // Unique ID for your channel
        private const val CHANNEL_NAME = "Template Channel Name" // User-visible name
        private const val CHANNEL_DESCRIPTION = "Template channel description" // User-visible description
    }
}