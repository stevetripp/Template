package com.example.template.ux.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.deeplink.CatalogDeepLinkRouter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * MainActivity for the app's main entry point.
 */
class MainActivity : ComponentActivity() {

    val mainViewModel: MainViewModel by viewModel()
    private val deepLinkRouteState = mutableStateOf<NavKey?>(null)

    private lateinit var inAppUpdateManagerUtil: InAppUpdateManagerUtil

    // Channel to trigger update snackbar from non-Compose code
    private val showSnackbarChannel = Channel<Unit>(Channel.CONFLATED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        deepLinkRouteState.value = intent?.dataString?.let { CatalogDeepLinkRouter.fromUri(it) }

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
                    MainScreen(deepLinkRouteState.value)
                }
            }
        }

        inAppUpdateManagerUtil = InAppUpdateManagerUtil(activity = this, inAppUpdateType = mainViewModel.inAppUpdateType, onCompleteUpdate = { showSnackbarChannel.trySend(Unit) })
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        deepLinkRouteState.value = intent.dataString?.let { CatalogDeepLinkRouter.fromUri(it) }
    }
}
