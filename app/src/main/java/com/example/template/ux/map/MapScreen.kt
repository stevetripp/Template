package com.example.template.ux.map

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MapUiState(val foo: StateFlow<String> = MutableStateFlow(""))

@Composable
fun MapScreen(navController: NavController, viewModel: MapViewModel = hiltViewModel()) {
    MapContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun MapContent(uiState: MapUiState, onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.MAP.title, onBack = onBack) }) { paddingValues ->
        val singapore = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        GoogleMap(
            modifier = Modifier.padding(paddingValues),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true),
            properties = MapProperties(isMyLocationEnabled = true)
        )
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { MapContent(MapUiState()) }
}