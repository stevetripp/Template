package com.example.template.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.Utils
import com.example.template.ui.theme.AppTheme
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun PermissionBanner(
    text: String,
    permission: String,
    modifier: Modifier = Modifier,
) {
    val permissionState = rememberPermissionState(permission)
    val context = LocalContext.current

    if (!permissionState.status.isGranted) {
        // If permission denied previously (shouldShowRationale == true) AND !isGranted:  This permission is now in a "Blocked" or "Never ask again"
        // state (prompt will no longer work)
        val permissionBlocked = permissionState.status.shouldShowRationale

        Banner(modifier = modifier, text = text) {
            // "Allow" was clicked...
            if (!permissionBlocked) {
                // Prompt the user in context
                permissionState.launchPermissionRequest()
            } else {
                // deep link intent right into the settings
                Utils.showSystemSettings(context)
            }
        }

        // check to see if we should prompt now
        if (!permissionBlocked) {
            SideEffect {
                permissionState.launchPermissionRequest()
            }
        }
    }
}

@Composable
private fun Banner(
    text: String,
    modifier: Modifier = Modifier,
    onAllowText: String = "Allow",
    onClick: () -> Unit = {},
    onAllow: (() -> Unit)? = null
) {
    Surface {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                text = text
            )
            onAllow?.let {
                TextButton(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp),
                    onClick = it
                ) {
                    Text(text = onAllowText.uppercase(), color = AppTheme.colors.primary)
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun BannerPreview() {
    AppTheme {
        Banner(
            text = "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec velit neque, auctor sit amet aliquam vel, ullamcorper sit amet ligula.",
            onAllow = {},
        )
    }
}