package com.example.template.ux.permissions

data class PermissionsUiState(
    val setPermissionsGranted: (Boolean) -> Unit = {},
)