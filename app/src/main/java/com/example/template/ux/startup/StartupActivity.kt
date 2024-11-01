package com.example.template.ux.startup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.MainActivity
import com.example.template.ux.main.getSharedMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.lds.mobile.ext.withLifecycleOwner

@AndroidEntryPoint
class StartupActivity : ComponentActivity() {

    private val viewModel: StartupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        collectFlows()
        viewModel.startup()

        setContent {
            val mainViewModel = getSharedMainViewModel()
            val enforceNavigationBarContrastState = mainViewModel.uiState.enforceNavigationBarContrastFlow.collectAsStateWithLifecycle()

            // https://developer.android.com/codelabs/edge-to-edge#2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = enforceNavigationBarContrastState.value
            }


            AppTheme { StartupScreen() }
        }
    }

    private fun collectFlows() = withLifecycleOwner(this) {
        viewModel.startupCompleteFlow.collectLatestWhenStarted {
            if (it) {
                val intent = Intent(baseContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                baseContext.startActivity(intent)
            }
        }
    }
}
