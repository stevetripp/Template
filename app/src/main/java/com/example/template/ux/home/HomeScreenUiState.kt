package com.example.template.ux.home

import com.example.template.ux.main.Screen

data class HomeScreenUiState(
    val onItemClicked: (Screen) -> Unit = {}
)