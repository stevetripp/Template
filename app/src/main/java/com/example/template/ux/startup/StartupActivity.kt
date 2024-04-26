package com.example.template.ux.startup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import org.lds.mobile.ext.withLifecycleOwner

@AndroidEntryPoint
class StartupActivity : ComponentActivity() {

    private val viewModel: StartupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        collectFlows()
        viewModel.startup()

        setContent {
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