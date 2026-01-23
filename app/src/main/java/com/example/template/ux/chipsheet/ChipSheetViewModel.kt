package com.example.template.ux.chipsheet

import androidx.lifecycle.ViewModel
import com.example.template.ui.widget.ChipItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ChipSheetViewModel : ViewModel() {

    private val numberItemsFlow = MutableStateFlow(
        listOf(
            ChipItem.Category("Numbers", "All"),
            ChipItem.Divider,
            ChipItem.Selectable("One"),
            ChipItem.Selectable("Two"),
            ChipItem.Selectable("Three"),
        )
    )

    private val animalItemsFlow = MutableStateFlow(
        listOf(
            ChipItem.Category("Animals", "All"),
            ChipItem.Divider,
            ChipItem.Selectable("dog"),
            ChipItem.Selectable("cat"),
            ChipItem.Selectable("elephant"),
            ChipItem.Selectable("lion"),
            ChipItem.Selectable("tiger"),
            ChipItem.Selectable("giraffe"),
            ChipItem.Selectable("monkey"),
            ChipItem.Selectable("dolphin"),
            ChipItem.Selectable("kangaroo"),
            ChipItem.Selectable("penguin"),
            ChipItem.Selectable("bear"),
            ChipItem.Selectable("horse"),
            ChipItem.Selectable("wolf"),
            ChipItem.Selectable("cheetah"),
            ChipItem.Selectable("gorilla"),
            ChipItem.Selectable("zebra"),
            ChipItem.Selectable("owl"),
            ChipItem.Selectable("snake"),
            ChipItem.Selectable("butterfly"),
            ChipItem.Selectable("octopus"),
        )
    )

    private val colorItemsFlow = MutableStateFlow(
        listOf(
            ChipItem.Category("Colors", "All"),
            ChipItem.Divider,
            ChipItem.Selectable("Red"),
            ChipItem.Selectable("Yellow"),
            ChipItem.Selectable("Blue"),
            ChipItem.Selectable("Green"),
        )
    )

    val uiState = ChipSheetUiState(
        numberItemsFlow = numberItemsFlow,
        animalItemsFlow = animalItemsFlow,
        colorItemsFlow = colorItemsFlow,
        onNumberItemSelected = { onChipSelected(it, numberItemsFlow) },
        onAnimalItemSelected = { onChipSelected(it, animalItemsFlow) },
        onColorSelected = ::onColorSelected
    )

    private fun MutableList<ChipItem>.replace(old: ChipItem, new: ChipItem) {
        set(indexOf(old), new)
    }

    private fun onChipSelected(selectedChip: ChipItem.Selectable, chipItemsFlow: MutableStateFlow<List<ChipItem>>) {
        chipItemsFlow.update { chipItems ->
            val mutableList = chipItems.toMutableList()

            when (selectedChip) {
                is ChipItem.Category -> {
                    if (!selectedChip.checked) {
                        mutableList.filterIsInstance<ChipItem.Selectable>().forEach {
                            if (it !is ChipItem.Category) mutableList.replace(it, it.copy(false))
                        }
                        mutableList.replace(selectedChip, selectedChip.copy(true))
                    }
                }

                else -> {
                    mutableList.replace(selectedChip, selectedChip.copy(!selectedChip.checked))
                    mutableList.filterIsInstance<ChipItem.Category>().firstOrNull()?.let { categoryChip ->
                        val checkedCount = mutableList.filterIsInstance<ChipItem.Selectable>().count { it.checked }
                        mutableList.replace(categoryChip, categoryChip.copy(checkedCount == 0))
                    }
                }
            }

            mutableList
        }
    }

    private fun onColorSelected(selected: ChipItem.Selectable) {
        colorItemsFlow.update { chipItems ->
            val mutableList = chipItems.toMutableList()
            mutableList.forEach { chipItem ->
                when (chipItem) {
                    is ChipItem.Selectable -> mutableList.replace(chipItem, chipItem.copy(chipItem.text == selected.text))
                    else -> Unit
                }
            }
            mutableList
        }
    }
}