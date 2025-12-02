package com.example.template.ux.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.example.template.ui.theme.AppTheme
import com.example.template.util.SmtLogger
import com.example.template.ux.pullrefresh.PullRefreshRoute
import com.example.template.ux.pullrefresh.deepLinkUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * MainActivity for the app's main entry point.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var inAppUpdateManagerUtil: InAppUpdateManagerUtil

    // Inject MainViewModel using Hilt
    private val mainViewModel: MainViewModel by viewModels()

    // Channel to trigger update snackbar from non-Compose code
    private val showSnackbarChannel = Channel<Unit>(Channel.CONFLATED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data

        SmtLogger.i(
            """appLinkIntent: $appLinkIntent
            |appLinkAction: $appLinkAction
            |appLinkData: $appLinkData
            |deepLinkUri: ${PullRefreshRoute.deepLinkUri}
        """.trimMargin()
        )

        val deepLinkRoute: NavKey? = when (appLinkData) {
            PullRefreshRoute.deepLinkUri -> PullRefreshRoute(closeOnBack = true)
            else -> null
        }

        SmtLogger.i("""deepLinkRoute: $deepLinkRoute""")

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
                            inAppUpdateManagerUtil.completeUpdate()
                        }
                    }
                }

                @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
                Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
                    MainScreen(deepLinkRoute)
                }
            }
        }

        inAppUpdateManagerUtil = InAppUpdateManagerUtil(activity = this, inAppUpdateType = mainViewModel.inAppUpdateType, onCompleteUpdate = { showSnackbarChannel.trySend(Unit) })
    }
}
