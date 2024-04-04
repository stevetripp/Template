package com.example.template.ux.permissions

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import com.google.accompanist.permissions.PermissionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel
@Inject constructor(
    application: Application
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    // Allows view model to get notified when permission changes thru system settings.
    private val hasPermissionGranted = MutableStateFlow(application.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

    val uiState = PermissionsUiState(
        onPermissionStatusChanged = { hasPermissionGranted.value = it is PermissionStatus.Granted }
    )
}
