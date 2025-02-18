package com.example.template.ux.fab

import com.example.template.model.data.SelectedObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class FabScreenUiState(
    val fabTypesFlow: StateFlow<List<SelectedObject<FabType>>> = MutableStateFlow(emptyList()),
    val onFabTypeChanged: (FabType) -> Unit = {},
)
