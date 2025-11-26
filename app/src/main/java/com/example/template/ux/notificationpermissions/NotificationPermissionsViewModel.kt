package com.example.template.ux.notificationpermissions

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.PermissionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

@HiltViewModel
class NotificationPermissionsViewModel
@Inject constructor(
    application: Application
) : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    // Allows view model to get notified when permission changes thru system settings.
    private val hasPermissionGranted = MutableStateFlow(application.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)

    val uiState = NotificationPermissionsUiState(
        onPermissionStatusChanged = { hasPermissionGranted.value = it is PermissionStatus.Granted }
    )
}