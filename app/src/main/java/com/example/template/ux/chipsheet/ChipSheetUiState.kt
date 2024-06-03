package com.example.template.ux.chipsheet

import com.example.template.ui.widget.ChipItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ChipSheetUiState(
    val numberItemsFlow: StateFlow<List<ChipItem>> = MutableStateFlow(emptyList()),
    val animalItemsFlow: StateFlow<List<ChipItem>> = MutableStateFlow(emptyList()),
    val colorItemsFlow: StateFlow<List<ChipItem>> = MutableStateFlow(emptyList()),
    val onNumberItemSelected: (ChipItem.Selectable) -> Unit = {},
    val onAnimalItemSelected: (ChipItem.Selectable) -> Unit = {},
    val onColorSelected: (ChipItem.Selectable) -> Unit = {}
)