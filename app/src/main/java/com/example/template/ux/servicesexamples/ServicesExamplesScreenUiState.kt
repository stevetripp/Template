package com.example.template.ux.servicesexamples

import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ServicesExamplesScreenUiState(
    val screensFlow: StateFlow<List<Screen>> = MutableStateFlow(emptyList()),
    val onItemClicked: (Screen) -> Unit = {}
)