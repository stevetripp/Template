package com.example.template.ux.permissions

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.Utils
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.PermissionBanner
import com.example.template.ux.main.Screen
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun PermissionsScreen(navController: NavController, viewModel: PermissionsViewModel = hiltViewModel()) {
    PermissionsContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun PermissionsContent(uiState: PermissionsUiState, onBack: () -> Unit = {}) {
    val inPreviewMode = LocalInspectionMode.current
    val context = if (!inPreviewMode) LocalContext.current else null

    if (!inPreviewMode) {
        val permissionState = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)
        LaunchedEffect(permissionState.status) { uiState.setPermissionsGranted(permissionState.status.isGranted) }
    }

    Scaffold(topBar = { AppTopAppBar(title = Screen.PERMISSIONS.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (!inPreviewMode) {
                PermissionBanner(
                    text = "[First Read] Please grant the permission",
                    permission = Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            Button(onClick = {
                if (!inPreviewMode) {
                    context?.let { Utils.showSystemSettings(it) }
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