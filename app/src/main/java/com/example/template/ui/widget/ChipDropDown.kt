package com.example.template.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun ChipDropDown(chipItems: List<ChipItem>, onSelected: (ChipItem.Selectable) -> Unit) {
    val selectedItem = chipItems.filterIsInstance<ChipItem.Selectable>().find { it.checked }
    val title = selectedItem?.text.orEmpty()
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        FilterChipWrapper(selected = isExpanded, highlighted = selectedItem !is ChipItem.Category, title = title, onClick = {
            isExpanded = !isExpanded
        })

        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }, modifier = Modifier.width(IntrinsicSize.Min)) {
            chipItems.forEach { chipItem ->
                chipItem.ShowDropdownMenuItem(onClicked = {
                    isExpanded = false
                    (chipItem as? ChipItem.Selectable)?.let { onSelected(it) }
                })
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview(
    @PreviewParameter(ChipItemsPreviewParameterProvider::class) previewItems: List<ChipItem>
) {
    var items by remember { mutableStateOf(previewItems) }

    AppTheme {
        Surface {
            ChipDropDown(chipItems = items, onSelected = { selected ->
                Log.i("SMT", "OnChipItem Clicked")
                items = items.map { chipItem ->
                    when (chipItem) {
                        ChipItem.Divider -> chipItem
                        is ChipItem.Selectable -> chipItem.apply { checked = text == selected.text }
                    }
                }
            })
        }
    }
}