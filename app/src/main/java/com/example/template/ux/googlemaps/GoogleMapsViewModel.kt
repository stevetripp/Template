package com.example.template.ux.googlemaps

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

class GoogleMapsViewModel : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {
    private val userLocationFlow = MutableStateFlow<LatLng?>(null)
    private val isLoadingLocationFlow = MutableStateFlow(false)

    val uiStateFlow: StateFlow<GoogleMapsUiState> = combine(
        userLocationFlow,
        isLoadingLocationFlow
    ) { userLocation, isLoadingLocation ->
        GoogleMapsUiState.Ready(
            userLocation = userLocation,
            isLoadingLocation = isLoadingLocation,
            onFetchUserLocation = ::fetchUserLocation
        )
    }.stateInDefault(viewModelScope, GoogleMapsUiState.Loading)

    @SuppressLint("MissingPermission")
    fun fetchUserLocation(context: Context) {
        isLoadingLocationFlow.value = true
        viewModelScope.launch {
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLocationFlow.value = LatLng(location.latitude, location.longitude)
                    } else {
                        // Default to Singapore if location is null
                        userLocationFlow.value = LatLng(1.35, 103.87)
                    }
                    isLoadingLocationFlow.value = false
                }.addOnFailureListener {
                    // Default to Singapore on error
                    userLocationFlow.value = LatLng(1.35, 103.87)
                    isLoadingLocationFlow.value = false
                }
            } catch (@Suppress("UNUSED_PARAMETER") e: Exception) {
                // Default to Singapore on exception
                userLocationFlow.value = LatLng(1.35, 103.87)
                isLoadingLocationFlow.value = false
            }
        }
    }
}
