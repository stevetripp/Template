package com.example.template.ux.permissions

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.PermissionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel
@Inject constructor(
    application: Application
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {

    // Allows view model to get notified when permission changes thru system settings.
    private val hasPermissionGranted = MutableStateFlow(application.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

    val uiState = PermissionsUiState(
        onPermissionStatusChanged = { hasPermissionGranted.value = it is PermissionStatus.Granted }
    )
}
