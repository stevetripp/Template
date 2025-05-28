package com.example.template.ux.reorderablelist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ReorderableListViewModel
@Inject constructor(
) : ViewModel() {

    private val listFlow = MutableStateFlow(List(100) { ReorderableItemData(it, "Item $it") })

    val uiState = ReorderableListUiState(
        listFlow = listFlow,
        onMove = ::onMove,
    )

    private fun onMove(fromIndex: Int, toIndex: Int) {
        val list = listFlow.value.toMutableList()
        val movedItem = list[fromIndex]
        list.removeAt(fromIndex)
        list.add(toIndex, movedItem)
        listFlow.value = list
    }
}

data class ReorderableItemData(val id: Int, val value: String)