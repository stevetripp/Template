package com.example.template.ux.startup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.MainActivity
import com.example.template.ux.main.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel
import org.lds.mobile.ext.withLifecycleOwner

class StartupActivity : ComponentActivity() {

    private val startupViewModel: StartupViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        enableEdgeToEdge()

        collectFlows()
        startupViewModel.startup()

        setContent {
            val mainViewModel: MainViewModel = koinViewModel()
            val enforceNavigationBarContrastState = mainViewModel.uiState.enforceNavigationBarContrastFlow.collectAsStateWithLifecycle()

            // https://developer.android.com/codelabs/edge-to-edge#2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = enforceNavigationBarContrastState.value
            }

            AppTheme { StartupScreen(viewModel = startupViewModel) }
        }
    }

    private fun collectFlows() = withLifecycleOwner(this) {
        startupViewModel.startupCompleteFlow.collectLatestWhenStarted {
            if (it) {
                val intent = Intent(baseContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                baseContext.startActivity(intent)
            }
        }
    }
}
