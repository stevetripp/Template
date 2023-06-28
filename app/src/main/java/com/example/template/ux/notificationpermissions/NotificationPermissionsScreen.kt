package com.example.template.ux.notificationpermissions

import android.Manifest
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.Utils
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.widget.PermissionBanner
import com.example.template.ux.main.Screen

@Composable
fun NotificationPermissionsScreen(navController: NavController, viewModel: NotificationPermissionsViewModel = hiltViewModel()) {
    NotificationPermissionsContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun NotificationPermissionsContent(uiState: NotificationPermissionsUiState, onBack: () -> Unit) {
    val inPreviewMode = LocalInspectionMode.current
    val context = if (!inPreviewMode) LocalContext.current else null

    Scaffold(topBar = { AppTopAppBar(title = Screen.NOTIFICATION_PERMISSIONS.title, onBack = onBack) }) { paddingValues ->
        if (!inPreviewMode) {
            PermissionBanner(
                modifier = Modifier.padding(paddingValues),
                text = "The App needs extra permissions to show notifications",
                permission = Manifest.permission.POST_NOTIFICATIONS,
                showSystemSettings = { context?.let { Utils.showAppNotificationsPermissions(it) } },
                onPermissionStatusChanged = uiState.onPermissionStatusChanged
            )
        }
    }
}