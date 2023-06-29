package com.example.template.ux.chipbottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.ChipBottomSheet
import com.example.template.ui.widget.ChipItem
import com.example.template.ux.main.Screen

@Composable
fun ChipBottomSheetScreen(navController: NavController) {
    ChipBottomSheetContent(navController::popBackStack)
}

@Composable
fun ChipBottomSheetContent(onBack: () -> Unit = {}) {
    var numberItems by remember {
        mutableStateOf(
            listOf(
                ChipItem.SelectableChipItem("Numbers", "All", isDefault = true, isSelected = true),
                ChipItem.DividerChipItem,
                ChipItem.SelectableChipItem("One", "One"),
                ChipItem.SelectableChipItem("Two", "Two"),
                ChipItem.SelectableChipItem("Three", "Three"),
            )
        )
    }

    var animalItems by remember {
        mutableStateOf(
            listOf(
                ChipItem.SelectableChipItem("Animals", "All", isDefault = true, isSelected = true),
                ChipItem.DividerChipItem,
                ChipItem.SelectableChipItem("Dog", "Dog"),
                ChipItem.SelectableChipItem("Cat", "Cat"),
                ChipItem.SelectableChipItem("Bird", "Bird"),
            )
        )
    }

    Scaffold(topBar = { AppTopAppBar(title = Screen.CHIP_BOTTOM_SHEET.title, onBack = onBack) }) { paddingValues ->
        Row(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ChipBottomSheet(chipItems = numberItems) { chipItem ->
                numberItems = numberItems.map {
                    when (it) {
                        is ChipItem.SelectableChipItem -> it.copy(isSelected = it.chipText == chipItem.chipText)
                        else -> it
                    }
                }
            }
            ChipBottomSheet(chipItems = animalItems) { chipItem ->
                animalItems = animalItems.map {
                    when (it) {
                        is ChipItem.SelectableChipItem -> it.copy(isSelected = it.chipText == chipItem.chipText)
                        else -> it
                    }
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun ChipBottomSheetContentPreview() {
    AppTheme { ChipBottomSheetContent() }
}