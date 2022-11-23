package com.example.template.ux.reorderablelist

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.burnoutcrew.reorderable.ItemPosition

data class ReorderableListUiState(
    val listFlow: StateFlow<List<ReoderableItemData>> = MutableStateFlow(emptyList()),
    val onMove: (ItemPosition, ItemPosition) -> Unit = { _, _ -> },
    val canDragOver: (ItemPosition, ItemPosition) -> Boolean = { _, _ -> true },
)