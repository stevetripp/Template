package com.example.template.ui.widget

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PermissionBanner(
    firstRead: String,
    rational: String,
    permission: String,
    permissionStateFlow: StateFlow<PermissionState>,
    updatePermissionState: (PermissionState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val permissionState by permissionStateFlow.collectAsState()
    val inPreviewMode = LocalInspectionMode.current


    if (!inPreviewMode) {
        val rememberedPermissionsState = rememberPermissionState(permission)
        Log.i(
            "SMT", """permissionState: $permissionState
            |rememberedPermissionsState.status: ${rememberedPermissionsState.status}
        """.trimMargin()
        )
        if (rememberedPermissionsState.status.isGranted) {
            updatePermissionState(PermissionState.Permitted)
            Banner(modifier = modifier, text = "Read contacts permission Granted")
        } else if (rememberedPermissionsState.status.shouldShowRationale && permissionState == PermissionState.NotPermitted) {
            Banner(
                modifier = modifier,
                text = rational,
                onClick = { rememberedPermissionsState.launchPermissionRequest() }
            ) { updatePermissionState(PermissionState.RejectedRationale) }
        } else if (permissionState == PermissionState.NotPermitted) {
            Banner(
                modifier = modifier,
                text = firstRead,
                onClick = rememberedPermissionsState::launchPermissionRequest,
            )
        }
    }
}

enum class PermissionState {
    NotPermitted,
    RejectedRequest,
    RejectedRationale,
    Permitted,
}

@Composable
private fun Banner(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDismiss: (() -> Unit)? = null
) {
    Surface {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                text = text
            )
            onDismiss?.let { TextButton(onClick = it) { Text(text = "Dismiss".uppercase(), color = AppTheme.colors.primary) } }
        }
    }
}

@PreviewDefault
@Composable
private fun BannerPreview() {
    AppTheme {
        Banner(
            text = "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec velit neque, auctor sit amet aliquam vel, ullamcorper sit amet ligula.",
            onDismiss = {},
        )
    }
}