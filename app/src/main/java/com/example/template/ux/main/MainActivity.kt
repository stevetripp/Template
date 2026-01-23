package com.example.template.ux.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.theme.AppTheme
import com.example.template.util.DeepLinkConstants
import com.example.template.util.SmtLogger
import com.example.template.ux.parameters.DestinationRoute
import com.example.template.ux.parameters.deepLinkPatterns
import com.example.template.ux.pullrefresh.PullRefreshRoute
import com.example.template.ux.pullrefresh.deepLinkPatterns
import io.ktor.http.Url
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.android.ext.android.inject
import org.lds.mobile.navigation3.DeepLink

/**
 * MainActivity for the app's main entry point.
 */
class MainActivity : ComponentActivity() {

    val mainViewModel: MainViewModel by inject()

    private lateinit var inAppUpdateManagerUtil: InAppUpdateManagerUtil

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
        """.trimMargin()
        )

        val sb = StringBuilder("Deep Link Patterns:\n")
        (PullRefreshRoute.deepLinkPatterns + DestinationRoute.deepLinkPatterns).forEach { sb.appendLine(it.url.toString()) }
        SmtLogger.i(sb.toString())

        // A Url doesn't handle anything other than https and http schemes. Replace the custom scheme for Url.
        val deepLink = appLinkData?.let { uri ->
            val uriString = uri.toString().replace(DeepLinkConstants.SCHEME_CUSTOM, DeepLinkConstants.SCHEME_HTTPS)
            DeepLink(Url(uriString))
        }

        SmtLogger.i("""deepLink: $deepLink""")

        val deepLinkRoute = deepLink?.let {
            PullRefreshRoute.deepLinkPatterns.find { deepLink.matches(it) }?.let { deepLink.toRoute<PullRefreshRoute>(it).copy(closeOnBack = true) }
                ?: DestinationRoute.deepLinkPatterns.find { deepLink.matches(it) }?.let { deepLink.toRoute<DestinationRoute>(it).copy(closeOnBack = true) }
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

        // Initialize InAppUpdateManagerUtil here where mainViewModel is in scope
        inAppUpdateManagerUtil = InAppUpdateManagerUtil(activity = this@MainActivity, inAppUpdateType = mainViewModel.inAppUpdateType, onCompleteUpdate = { showSnackbarChannel.trySend(Unit) })
    }
}
