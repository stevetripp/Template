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
import com.example.template.ui.widget.ChipItem
import com.example.template.ui.widget.ChipModalBottomSheet
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
                ChipItem.Category("Numbers", "All"),
                ChipItem.Divider,
                ChipItem.Selectable("One"),
                ChipItem.Selectable("Two"),
                ChipItem.Selectable("Three"),
            )
        )
    }

    var animalItems by remember {
        mutableStateOf(
            listOf(
                ChipItem.Category("Animals", "All"),
                ChipItem.Divider,
                ChipItem.Selectable("Dog"),
                ChipItem.Selectable("Cat"),
                ChipItem.Selectable("Bird"),
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
            ChipModalBottomSheet(chipItems = numberItems, onItemsChanged = { numberItems = it })
            ChipModalBottomSheet(chipItems = animalItems, onItemsChanged = { animalItems = it })
        }
    }
}

@PreviewDefault
@Composable
private fun ChipBottomSheetContentPreview() {
    AppTheme { ChipBottomSheetContent() }
}