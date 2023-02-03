package com.example.template.ux.permissions

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import com.example.template.ui.navigation.ViewModelNav
import com.example.template.ui.navigation.ViewModelNavImpl
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
        setPermissionsGranted = { hasPermissionGranted.value = it }
    )
}
