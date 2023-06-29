package com.example.template.ui.widget

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun ChipBottomSheet(chipItems: List<ChipItem>, onSelected: (ChipItem.SelectableChipItem) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedChipItem = chipItems.find { it is ChipItem.SelectableChipItem && it.isSelected } as? ChipItem.SelectableChipItem
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    FilterChipWrapper(selected = showBottomSheet, highlighted =selectedChipItem?.isDefault != true, title = selectedChipItem?.chipText.orEmpty(), onClick = { showBottomSheet = !showBottomSheet })

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { showBottomSheet = false }) {
            LazyColumn {
                items(chipItems) { chipItem ->
                    when (chipItem) {
                        ChipItem.DividerChipItem -> Divider()
                        is ChipItem.SelectableChipItem -> {
                            ChipBottomSheetItem(chipItem = chipItem) {
                                scope.launch {
                                    bottomSheetState.hide() // Added for animation
                                    showBottomSheet = false
                                    onSelected(chipItem)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChipBottomSheetItem(chipItem: ChipItem.SelectableChipItem, onClicked: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable { onClicked() },
        headlineContent = { Text(chipItem.bottomSheetText) },
        trailingContent = {
            val icon = if (chipItem.isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank
            Icon(icon, contentDescription = "Localized description")
        }
    )
}

@PreviewDefault
@Composable
private fun ChipBottomSheetPreview() {
    var items by remember {
        mutableStateOf(
            listOf(
                ChipItem.SelectableChipItem("Category", "All", isDefault = true, isSelected = true),
                ChipItem.DividerChipItem,
                ChipItem.SelectableChipItem("One", "One"),
                ChipItem.SelectableChipItem("Two", "Two"),
                ChipItem.SelectableChipItem("Three", "Three"),
            )
        )
    }

    AppTheme {
        Surface {
            ChipBottomSheet(chipItems = items) { chipItem ->
                Log.i("SMT", "OnChipItem Clicked")
                items = items.map {
                    when (it) {
                        is ChipItem.SelectableChipItem -> it.copy(isSelected = it.chipText == chipItem.chipText)
                        else -> it
                    }
                }
            }
        }
    }
}

sealed class ChipItem {
    data class SelectableChipItem(val chipText: String, val bottomSheetText: String, val obj: Any? = null, val isDefault: Boolean = false, val isSelected: Boolean = false) : ChipItem()
    object DividerChipItem : ChipItem()
}