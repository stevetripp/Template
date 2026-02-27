package com.example.template.ux.googlemaps

import android.content.Context
import com.google.android.gms.maps.model.LatLng

sealed interface GoogleMapsUiState {
    data object Loading : GoogleMapsUiState

    data class Ready(
        val userLocation: LatLng? = null,
        val isLoadingLocation: Boolean = false,
        val onFetchUserLocation: (Context) -> Unit = {},
    ) : GoogleMapsUiState
}
