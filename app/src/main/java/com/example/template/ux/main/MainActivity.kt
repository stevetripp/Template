package com.example.template.ux.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.theme.AppTheme
import com.example.template.util.SmtLogger
import com.example.template.ux.settings.InAppUpdateType
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

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

    // Inject MainViewModel using Hilt
    private val mainViewModel: MainViewModel by viewModels()

    // ActivityResultLauncher for in-app update
    private val updateResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        // Handle the result of the update flow
        if (result.resultCode == RESULT_OK) {
            SmtLogger.i("In-app update completed successfully.")
        } else {
            SmtLogger.i("In-app update flow was cancelled or failed. Result code: ${result.resultCode}")
            if (mainViewModel.inAppUpdateType == InAppUpdateType.IMMEDIATE) {
                // When IMMEDIATE don't allow the user to continue to use app
                finishAffinity()
            }
        }
    }

    // Channel to trigger update snackbar from non-Compose code
    private val showSnackbarChannel = Channel<Unit>(Channel.CONFLATED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

        enableEdgeToEdge()

        setContent {
            val enforceNavigationBarContrastState = mainViewModel.uiState.enforceNavigationBarContrastFlow.collectAsStateWithLifecycle()

            // https://developer.android.com/codelabs/edge-to-edge#2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = enforceNavigationBarContrastState.value
            }

            AppTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                // Listen for update snackbar events
                LaunchedEffect(Unit) {
                    showSnackbarChannel.receiveAsFlow().collect {
                        val result = snackbarHostState.showSnackbar(
                            message = "An update has just been downloaded.",
                            actionLabel = "RESTART",
                            withDismissAction = true
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            appUpdateManager.completeUpdate()
                        }
                    }
                }

                Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
                    MainScreen()
                }
            }
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
        if (updateAvailability == UpdateAvailability.UPDATE_AVAILABLE) {
            if (mainViewModel.inAppUpdateType == InAppUpdateType.IMMEDIATE && appUpdateInfo.isImmediateUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            } else if (mainViewModel.inAppUpdateType == InAppUpdateType.FLEXIBLE && appUpdateInfo.isFlexibleUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                )
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                // An update has been downloaded, but not installed. Prompt user to install the update
                showSnackbarChannel.trySend(Unit)
            }
        }
    }

    override fun onResume() {
        SmtLogger.i("""onResume called!""")
        super.onResume()

        if (mainViewModel.inAppUpdateType == InAppUpdateType.FLEXIBLE) {
            // Register the listener to monitor the state when the app is in the foreground
            appUpdateManager.registerListener { state ->
                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    // When the update is downloaded, prompt the user to install it.
                    showSnackbarChannel.trySend(Unit)
                }
            }
        }
    }
}
