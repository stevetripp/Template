package com.example.template.ux.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

@Composable
fun NotificationScreen(navController: NavController, viewModel: NotificationViewModel = hiltViewModel()) {
    NotificationContent(viewModel.uiState, onBack = navController::popBackStack)
}

@Composable
private fun NotificationContent(uiState: NotificationUiState, onBack: () -> Unit = {}) {
    val context = LocalContext.current

    Scaffold(topBar = { AppTopAppBar(title = Screen.NOTIFICATION.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            TextButton(onClick = { uiState.onCreateNotification(context) }) { Text(text = "Send Notification") }
            TextButton(onClick = { uiState.onCreateDeepLinkNotification(context) }) { Text(text = "Send Deep Link Notification") }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { NotificationContent(NotificationUiState()) }
}