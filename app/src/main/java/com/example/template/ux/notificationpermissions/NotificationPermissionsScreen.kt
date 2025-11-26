package com.example.template.ux.notificationpermissions

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.example.template.ui.Utils
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.widget.PermissionBanner
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun NotificationPermissionsScreen(navigator: Navigation3Navigator, viewModel: NotificationPermissionsViewModel) {
    NotificationPermissionsContent(viewModel.uiState, navigator::pop)
}

@Composable
fun NotificationPermissionsContent(uiState: NotificationPermissionsUiState, onBack: () -> Unit) {
    val inPreviewMode = LocalInspectionMode.current
    val context = if (!inPreviewMode) LocalContext.current else null

    Scaffold(topBar = { AppTopAppBar(title = Screen.NOTIFICATION_PERMISSIONS.title, onBack = onBack) }) { paddingValues ->
        if (!inPreviewMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
}