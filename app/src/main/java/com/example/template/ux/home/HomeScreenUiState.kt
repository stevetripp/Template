package com.example.template.ux.home

import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HomeScreenUiState(
    val screensFlow: StateFlow<List<Screen>> = MutableStateFlow(emptyList()),
    val onItemClicked: (Screen) -> Unit = {},
    val onSettingsClicked: () -> Unit = {},
)