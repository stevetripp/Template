package com.example.template.ux.home

import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HomeScreenUiState(
    val inputScreensFlow: StateFlow<List<Screen>>,
    val navigationScreensFlow: StateFlow<List<Screen>>,
    val visualScreensFlow: StateFlow<List<Screen>>,
    val selectedTabFlow: MutableStateFlow<Int> = MutableStateFlow(0),
    val onItemClicked: (Screen) -> Unit = {},
    val onSettingsClicked: () -> Unit = {},
)