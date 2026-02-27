package com.example.template.ux.googlemaps

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.android.gms.maps.model.LatLng

class GoogleMapsUiStatePreviewParameterProvider : PreviewParameterProvider<GoogleMapsUiState> {
    override val values: Sequence<GoogleMapsUiState> = sequenceOf(
        GoogleMapsUiState.Loading,
        GoogleMapsUiState.Ready(
            userLocation = LatLng(1.35, 103.87),
            isLoadingLocation = false
        )
    )
}
