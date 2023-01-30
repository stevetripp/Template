package com.example.template.ux.permissions

import com.example.template.ui.widget.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PermissionsUiState(
    val permissionStateFlow: StateFlow<PermissionState> = MutableStateFlow(PermissionState.NotPermitted),
    val updatePermissionState: (PermissionState) -> Unit = {},
)