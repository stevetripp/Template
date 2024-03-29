package com.example.template.ux.permissions

import com.google.accompanist.permissions.PermissionStatus

data class PermissionsUiState(
    val onPermissionStatusChanged: (PermissionStatus) -> Unit = {},
)