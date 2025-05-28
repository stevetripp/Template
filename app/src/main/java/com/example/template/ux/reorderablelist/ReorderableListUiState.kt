package com.example.template.ux.reorderablelist

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ReorderableListUiState(
    val listFlow: StateFlow<List<ReorderableItemData>> = MutableStateFlow(emptyList()),
    val onMove: (Int, Int) -> Unit = { _, _ -> }
)