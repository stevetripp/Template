package com.example.template.ux.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.theme.AppTheme
import com.example.template.util.SmtLogger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        SmtLogger.i("""intent: ${intent.action}, ${intent.data}""")
        intent.data?.let { viewModel.navigateToDeepLink(it) }
    }
}