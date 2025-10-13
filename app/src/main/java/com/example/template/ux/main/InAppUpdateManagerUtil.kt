package com.example.template.ux.main

import android.app.Activity.RESULT_OK
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.template.util.SmtLogger
import com.example.template.ux.settings.InAppUpdateType
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed

/**
 * Utility class to manage in-app updates using the Google Play Core library.
 *
 * This class supports both FLEXIBLE and IMMEDIATE update types, handling update flow,
 * lifecycle registration, and update completion callbacks. It is designed to be used
 * with a [ComponentActivity] and integrates with the Android lifecycle for FLEXIBLE updates.
 *
 * ## Usage
 * Instantiate this class in your Activity, providing the desired [InAppUpdateType] and a
 * callback to be invoked when the update is downloaded and ready to complete (for FLEXIBLE updates).
 *
 * Example:
 * ```kotlin
 * val updateManager = InAppUpdateManagerUtil(
 *     activity = this,
 *     inAppUpdateType = InAppUpdateType.FLEXIBLE,
 *     onCompleteUpdate = { showSnackbarToRestartApp() }
 * )
 * ```
 *
 * ## Features
 * - Checks for available updates and starts the update flow if allowed
 * - Handles both FLEXIBLE and IMMEDIATE update types
 * - Registers/unregisters listeners for FLEXIBLE updates based on lifecycle
 * - Calls [onCompleteUpdate] when an update is downloaded and ready to install
 * - Handles user cancellation or failure of the update flow
 *
 * ## Safety
 * - For IMMEDIATE updates, the app is closed if the user cancels the update
 *
 * ## Testing
 * - In-app update developer instructions to test with https://play.google.com/console/internal-app-sharing
 * - Must enable developer options in the Play Store App
 *   - Settings | About | Tap "Play Store version" 7 times to enable developer options
 *   - General | Developer Options | Enable "Internal app sharing"
 * - Must bump version code before building release APK
 *
 * @param activity The [ComponentActivity] context
 * @param inAppUpdateType The desired update type (FLEXIBLE or IMMEDIATE)
 * @param onCompleteUpdate Callback invoked when an update is downloaded and ready to complete
 */
class InAppUpdateManagerUtil(
    private val activity: ComponentActivity,
    private val inAppUpdateType: InAppUpdateType,
    private val onCompleteUpdate: () -> Unit
) : DefaultLifecycleObserver {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity.applicationContext)

    /**
     * Initializes the InAppUpdateManagerUtil.
     */
    init {
        // Asynchronously obtains the AppUpdateInfo object.
        // This object contains information about the availability of an update and the type of update.
        // The ::onAppUpdateInfo method will be called when the information is successfully retrieved.
        appUpdateManager.appUpdateInfo.addOnSuccessListener(::onAppUpdateInfo)
        if (inAppUpdateType == InAppUpdateType.FLEXIBLE) {
            activity.lifecycle.addObserver(this)
        }
    }

    private val updateResultLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        // Handle the result of the update flow
        if (result.resultCode == RESULT_OK) {
            // NOOP
        } else {
            if (inAppUpdateType == InAppUpdateType.IMMEDIATE) {
                // When IMMEDIATE don't allow the user to continue to use app
                activity.finishAffinity()
            }
        }
    }

    private fun onAppUpdateInfo(appUpdateInfo: AppUpdateInfo) {
        val updateAvailability = appUpdateInfo.updateAvailability()

        SmtLogger.i(
            """updateAvailability = $updateAvailability
            |installStatus = ${appUpdateInfo.installStatus()}
            |isImmediateUpdateAllowed: ${appUpdateInfo.isImmediateUpdateAllowed}
            |isFlexibleUpdateAllowed: ${appUpdateInfo.isFlexibleUpdateAllowed}
        """.trimIndent()
        )

        // Checks if update is available and of the desired type (FLEXIBLE or IMMEDIATE)
        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
            // An update has been downloaded, but not installed. Prompt user to install the update
            onCompleteUpdate()
        } else if (updateAvailability == UpdateAvailability.UPDATE_AVAILABLE &&
            ((inAppUpdateType == InAppUpdateType.IMMEDIATE && appUpdateInfo.isImmediateUpdateAllowed) ||
                    (inAppUpdateType == InAppUpdateType.FLEXIBLE && appUpdateInfo.isFlexibleUpdateAllowed))
        ) {
            inAppUpdateType.appUpdateType?.let { appUpdateType ->
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateResultLauncher,
                    AppUpdateOptions.newBuilder(appUpdateType).build()
                )
            }
        }
    }

    /**
     * Completes a downloaded in-app update.
     *
     * This method should be called after a FLEXIBLE update has been downloaded and the user
     * has confirmed they are ready to restart the app to apply the update. It triggers the
     * installation of the update package and restarts the app as required by the Play Core library.
     *
     * For IMMEDIATE updates, this is handled automatically by the system and does not need to be called.
     *
     * @see <a href="https://developer.android.com/guide/playcore/in-app-updates/kotlin-java">In-app updates documentation</a>
     */
    fun completeUpdate() = appUpdateManager.completeUpdate()

    private val installStateUpdatedListener = { state: InstallState ->
        SmtLogger.i("""installStatus(): ${state.installStatus()}""")
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            onCompleteUpdate()
        }
    }

    /**
     * Registers the install state listener when the activity is resumed.
     * This is only used for FLEXIBLE updates.
     */
    override fun onResume(owner: LifecycleOwner) {
        SmtLogger.i("""onResume""")
        appUpdateManager.registerListener(installStateUpdatedListener)
    }

    /**
     * Unregisters the install state listener when the activity is paused.
     * This is only used for FLEXIBLE updates.
     */
    override fun onPause(owner: LifecycleOwner) {
        SmtLogger.i("""onPause""")
        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }
}