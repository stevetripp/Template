package com.example.template.ux.googlemaps

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.Utils
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.PermissionsBanner
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import org.lds.mobile.navigation3.navigator.Navigation3Navigator
import org.lds.mobile.ui.compose.navigation.HandleNavigation3

@Composable
fun GoogleMapsScreen(navigator: Navigation3Navigator, viewModel: GoogleMapsViewModel) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    GoogleMapsScreenContent(uiState = uiState, onBack = navigator::pop)
    HandleNavigation3(viewModelNavigation = viewModel, navigator = navigator)
}

@Composable
private fun GoogleMapsScreenContent(
    uiState: GoogleMapsUiState,
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Google Maps",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is GoogleMapsUiState.Loading -> LoadingContent(paddingValues)
            is GoogleMapsUiState.Ready -> ReadyContent(uiState, paddingValues)
        }
    }
}

@Composable
private fun LoadingContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ReadyContent(
    uiState: GoogleMapsUiState.Ready,
    paddingValues: PaddingValues
) {
    val inPreviewMode = LocalInspectionMode.current
    val context = if (!inPreviewMode) LocalContext.current else null

    val permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val permissionState = if (!inPreviewMode) rememberMultiplePermissionsState(permissions) else null
    val locationPermissionGranted = permissionState?.allPermissionsGranted ?: false

    // Fetch user location when composition launches and permission is granted
    LaunchedEffect(key1 = locationPermissionGranted) {
        if (locationPermissionGranted && context != null) {
            uiState.onFetchUserLocation(context)
        }
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        if (!inPreviewMode && !locationPermissionGranted) {
            PermissionsBanner(
                text = "This screen requires location permission to show your location on the map",
                permissions = permissions,
                showSystemSettings = { context?.let { Utils.showSystemSettings(it) } }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoadingLocation) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Use user location if available, otherwise default to Singapore
            val mapCenter = uiState.userLocation ?: LatLng(1.35, 103.87)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(mapCenter, 10f)
            }

            // Update camera position when userLocation changes
            LaunchedEffect(uiState.userLocation) {
                uiState.userLocation?.let {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f)
                }
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = locationPermissionGranted
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = locationPermissionGranted
                )
            )
        }
    }
}

@PreviewDefault
@Composable
private fun GoogleMapsScreenPreview(
    @PreviewParameter(GoogleMapsUiStatePreviewParameterProvider::class) uiState: GoogleMapsUiState
) {
    AppTheme {
        GoogleMapsScreenContent(uiState = uiState)
    }
}
