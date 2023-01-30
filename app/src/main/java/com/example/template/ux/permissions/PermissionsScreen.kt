package com.example.template.ux.permissions

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.PermissionBanner
import com.example.template.ux.main.Screen

@Composable
fun PermissionsScreen(navController: NavController, viewModel: PermissionsViewModel = hiltViewModel()) {
    PermissionsContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun PermissionsContent(uiState: PermissionsUiState, onBack: () -> Unit = {}) {
    val inPreviewMode = LocalInspectionMode.current
    val context = if (!inPreviewMode) LocalContext.current else null

    Scaffold(topBar = { AppTopAppBar(title = Screen.PERMISSIONS.title, onBack = onBack) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            PermissionBanner(
                firstRead = "[First Read] Please grant the permission",
                rational = "[Rationale] Please grant the permission.",
                permission = Manifest.permission.ACCESS_COARSE_LOCATION,
                permissionStateFlow = uiState.permissionStateFlow,
                updatePermissionState = uiState.updatePermissionState
            )
            Button(onClick = {
                if (!inPreviewMode) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        data = Uri.fromParts("package", context?.packageName.orEmpty(), null)
                    }
                    context?.startActivity(intent)
                }
            }) {
                Text("App Settings")
            }
        }
    }
}

@PreviewDefault
@Composable
private fun PermissionsContentPreview() {
    AppTheme { PermissionsContent(PermissionsUiState()) }
}