package com.example.template.ux.startup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun StartupScreen(viewModel: StartupViewModel) {
    StartupContent(countdownFlow = viewModel.countdownFlow)
}

@Composable
fun StartupContent(countdownFlow: StateFlow<Int>) {
    val countdown by countdownFlow.collectAsStateWithLifecycle()
    Surface {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Startup Screen")
            Text(text = countdown.toString())
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { StartupContent(MutableStateFlow(3)) }
}