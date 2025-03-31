package com.example.template.ux.map

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel(){
    val uiState = MapUiState()
}