package com.example.template.ux.permissions

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
class PermissionsViewModel
@Inject constructor(
    application: Application
) : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    // Allows view model to get notified when permission changes thru system settings.
    private val hasPermissionGranted = MutableStateFlow(application.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

    val uiState = PermissionsUiState(
        onPermissionStatusChanged = { hasPermissionGranted.value = it is PermissionStatus.Granted }
    )
}
