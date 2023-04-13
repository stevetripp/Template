package com.example.template.ux.notificationpermissions

import com.google.accompanist.permissions.PermissionStatus

data class NotificationPermissionsUiState(
    val onPermissionStatusChanged: (PermissionStatus) -> Unit = {},
)