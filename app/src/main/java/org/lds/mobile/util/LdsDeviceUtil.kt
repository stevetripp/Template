@file:Suppress("unused")

package org.lds.mobile.util

import android.Manifest
import android.app.Application
import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import androidx.biometric.BiometricManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import co.touchlab.kermit.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LdsDeviceUtil @Inject
constructor(private val application: Application) {

    /**
     * Returns the package name for the App Store (install source) that was used to install this app
     */
    fun getInstallSource(): String {
        val packageName = application.packageManager.getInstallerPackageName(application.packageName)
        return when {
            packageName != null -> packageName
            else -> "Unknown"
        }
    }

    /**
     * Determines the current network connection for the device
     *
     * @return The string representation for the connected network
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun getNetworkInfoText(): String {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null) {
            val extraInfo = if (networkInfo.extraInfo == null) "" else networkInfo.extraInfo
            return """${networkInfo.typeName} ${networkInfo.subtypeName} ${networkInfo.detailedState} $extraInfo"""
        }

        return "Unknown"
    }

    fun isTv(): Boolean {
        val uiModeManager = application.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
    }

    fun isFireTv() = application.packageManager.hasSystemFeature("amazon.hardware.fire_tv")

    fun isPackageInstalled(packageName: String): Boolean {
        return try {
            application.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (ignore: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getPackageVersionName(packageName: String): String? {
        return try {
            application.packageManager.getPackageInfo(packageName, 0).versionName
        } catch (ignore: PackageManager.NameNotFoundException) {
            "$packageName not found"
        }
    }

    fun appInstalledFromAmazonAppStore(): Boolean {
        return when (application.packageManager.getInstallerPackageName(application.packageName)) {
            GOOGLE_PLAY_PACKAGE -> false
            AMAZON_APPSTORE_PACKAGE -> true
            else -> false
        }
    }

    // used to prevent an exception when play store cannot handle intents
    fun canHandlePlayStoreIntent(): Boolean {
        val intent = application.packageManager.getLaunchIntentForPackage(GOOGLE_PLAY_PACKAGE) ?: return false
        val list = application.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.isNotEmpty()
    }

    /**
     * Determines the current devices display size
     *
     * @return The String representation for the display size in the format (width x height)
     */
    fun getDisplaySizeText(context: Context): String {
        try {
            val display = context.resources.displayMetrics
            return display.widthPixels.toString() + "x" + display.heightPixels
        } catch (expected: Exception) {
            Logger.e(expected) { "Unable to retrieve device display size" }
        }

        return "Unknown"
    }

    /**
     * Detect if device is ARC (Android Runtime Chromium ie ChromeBooks)
     * See https://github.com/google/science-journal/blob/master/OpenScienceJournal/whistlepunk_library/src/main/java/com/google/android/apps/forscience/whistlepunk/MultiWindowUtils.java
     */
    fun isARC(): Boolean {
        return application.packageManager.hasSystemFeature(CHROMIUM_ARC_DEVICE_MANAGEMENT)
    }

    fun supportsBiometric(context: Context): Boolean {
        return BiometricManager.from(context).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Check to see if Google Play Services are installed and available
     */
    fun isGooglePlayServicesAvailable(): Boolean {
        val availability = GoogleApiAvailability.getInstance()
        val resultCode = availability.isGooglePlayServicesAvailable(application)
        if (resultCode != ConnectionResult.SUCCESS) {
            return false
        }
        return true
    }

    /**
     * Determine if the device supports picture-in-picture
     * We have decided not to support picture-in-picture on chromebooks
     * @return true if the device supports picture-in-picture, false if it does not
     */
    fun supportsPictureInPicture(): Boolean {
        return application.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }

    companion object {
        const val GOOGLE_MARKET_BASE_URI = "market://details?id="
        const val AMAZON_MARKET_BASE_URI = "http://www.amazon.com/gp/mas/dl/android?p="

        const val ANDROID_SYSTEM_WEBVIEW_PACKAGE = "com.google.android.webview"
        const val CHROME_APP_PACKAGE = "com.android.chrome"

        const val BOOK_OF_MORMON_PACKAGE = "org.lds.bom"
        const val GOSPEL_LIBRARY_PACKAGE = "org.lds.ldssa"
        const val LDS_MUSIC_PACKAGE = "org.lds.ldsmusic"
        const val LDS_TOOLS_PACKAGE = "org.lds.ldstools"
        const val MORMON_CHANNEL_PACKAGE = "org.lds.mormonchannel.client.android"
        const val DOCTRINAL_MASTERY_PACKAGE = "org.lds.sm"
        const val LDS_MEDIA_LIBRARY_PACKAGE = "org.lds.medialibrary"

        const val GOOGLE_PLAY_PACKAGE = "com.android.vending"
        const val AMAZON_APPSTORE_PACKAGE = "com.amazon.venezia"

        const val CHROMIUM_ARC_DEVICE_MANAGEMENT = "org.chromium.arc.device_management"
    }
}
