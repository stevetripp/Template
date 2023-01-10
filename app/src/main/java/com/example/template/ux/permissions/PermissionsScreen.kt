package com.example.template.ux.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PermissionsScreen(navController: NavController) {
    PermissionsContent(navController::popBackStack)
}

@Composable
private fun PermissionsContent(onBack: () -> Unit = {}) {
    val readContactsPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_CONTACTS
    )

    Scaffold(
        topBar = { AppTopAppBar(title = Screen.PERMISSIONS.title, onBack = onBack) }) {
        Column(modifier = Modifier
            .padding(it)
            .padding(16.dp)
        ) {

            if (readContactsPermissionState.status.isGranted) {
                Text("Read contacts permission Granted")
            } else {
                val textToShow = if (readContactsPermissionState.status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "[Rationale] Reading contacts is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "[First Read] Read contact permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { readContactsPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun PermissionsContentPreview() {
    AppTheme { PermissionsContent() }
}