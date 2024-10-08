package com.example.template.ux.reorderablelist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.burnoutcrew.reorderable.ItemPosition
import javax.inject.Inject

@HiltViewModel
class ReorderableListViewModel
@Inject constructor(
) : ViewModel() {

    private val listFlow = MutableStateFlow(List(100) { ReorderableItemData(it, "Item $it") })

    val uiState = ReorderableListUiState(
        listFlow = listFlow,
        onMove = ::onMove,
        canDragOver = { _, _ -> true },
    )

    private fun onMove(from: ItemPosition, to: ItemPosition) {
        val list = listFlow.value.toMutableList()
        val movedItem = list[from.index]
        list.removeAt(from.index)
        list.add(to.index, movedItem)
        listFlow.value = list
    }
}

data class ReorderableItemData(val id: Int, val value: String)