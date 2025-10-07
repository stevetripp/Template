package com.example.template.ux.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.theme.AppTheme
import com.example.template.util.SmtLogger
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity for the app's main entry point.
 *
 * ## In-app update developer instructions to test with https://play.google.com/console/internal-app-sharing
 * - Must enable developer options in the Play Store App
 *   - Settings | About | Tap "Play Store version" 7 times to enable developer options
 *   - General | Developer Options | Enable "Internal app sharing"
 * - Must bump version code before building release APK
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var appUpdateManager: AppUpdateManager

    // ActivityResultLauncher for in-app update
    private val immediateUpdateResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        // Handle the result of the update flow
        if (result.resultCode == RESULT_OK) {
            SmtLogger.i("In-app update completed successfully.")
        } else {
            SmtLogger.i("In-app update flow was cancelled or failed. Result code: ${result.resultCode}")
            finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

        enableEdgeToEdge()

        setContent {
            val mainViewModel = getSharedMainViewModel()
            val enforceNavigationBarContrastState = mainViewModel.uiState.enforceNavigationBarContrastFlow.collectAsStateWithLifecycle()

            // https://developer.android.com/codelabs/edge-to-edge#2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = enforceNavigationBarContrastState.value
            }

            AppTheme { MainScreen() }
        }

        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data

        SmtLogger.i(
            """appLinkIntent: $appLinkIntent
            |appLinkAction: $appLinkAction
            |appLinkData: $appLinkData
        """.trimMargin()
        )

        appUpdateManager.appUpdateInfo.addOnSuccessListener(::onAppUpdateInfo)
    }

    private fun onAppUpdateInfo(appUpdateInfo: AppUpdateInfo) {

        val updateAvailability = appUpdateInfo.updateAvailability()

        SmtLogger.i(
            """appUpdateInfo.updateAvailability(): $updateAvailability
            |appUpdateInfo.isFlexibleUpdateAllowed: ${appUpdateInfo.isFlexibleUpdateAllowed}
            |appUpdateInfo.isImmediateUpdateAllowed: ${appUpdateInfo.isImmediateUpdateAllowed}
        """.trimMargin()
        )

        // Checks if update is available and of the desired type (FLEXIBLE or IMMEDIATE)
        if (updateAvailability == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isImmediateUpdateAllowed) {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                immediateUpdateResultLauncher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        }
    }
}
