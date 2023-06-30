package com.example.template.ux.chipsheet

import android.util.Log
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
import com.example.template.ui.widget.ChipDropDown
import com.example.template.ui.widget.ChipItem
import com.example.template.ui.widget.ChipModalBottomSheet
import com.example.template.ux.main.Screen

@Composable
fun ChipSheetScreen(navController: NavController) {
    ChipSheetContent(navController::popBackStack)
}

@Composable
fun ChipSheetContent(onBack: () -> Unit = {}) {
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
    }

    var colorItems by remember {
        mutableStateOf(
            listOf(
                ChipItem.Category("Colors", "All"),
                ChipItem.Divider,
                ChipItem.Selectable("Red"),
                ChipItem.Selectable("Yellow"),
                ChipItem.Selectable("Blue"),
                ChipItem.Selectable("Green"),
            )
        )
    }

    Log.i("SMT", "Recompose")
    Scaffold(topBar = { AppTopAppBar(title = Screen.CHIP_SHEET.title, onBack = onBack) }) { paddingValues ->
        Row(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ChipModalBottomSheet(chipItems = numberItems, onItemsChanged = {
                Log.i("SMT", "ChipModalBottomSheet.onItemsChanged")
                numberItems = it
            })
            ChipModalBottomSheet(chipItems = animalItems, onItemsChanged = { animalItems = it })
            ChipDropDown(chipItems = colorItems, onSelected = { selected ->
                colorItems = colorItems.map { chipItem ->
                    when (chipItem) {
                        ChipItem.Divider -> chipItem
                        is ChipItem.Selectable -> chipItem.apply { checked = text == selected.text }
                    }
                }
            })
        }
    }
}

@PreviewDefault
@Composable
private fun ChipBottomSheetContentPreview() {
    AppTheme { ChipSheetContent() }
}