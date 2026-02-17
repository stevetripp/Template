package com.example.template.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun ChipDropDown(chipItems: List<ChipItem>, onSelected: (ChipItem.Selectable) -> Unit) {
    val selectedItem = chipItems.filterIsInstance<ChipItem.Selectable>().find { it.checked }
    val title = selectedItem?.text.orEmpty()
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        FilterChipWrapper(selected = selectedItem !is ChipItem.Category, title = title, onClick = { isExpanded = !isExpanded })
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }, modifier = Modifier.width(IntrinsicSize.Min)) {
            chipItems.forEach { chipItem ->
                chipItem.showDropdownMenuItem(onClicked = {
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
    val items1 by remember { mutableStateOf(previewItems) }
    val items2 by remember {
        mutableStateOf(previewItems.map {
            when (it) {
                ChipItem.Divider -> it
                is ChipItem.Category -> it.copy(false)
                is ChipItem.Selectable -> it.copy(it.text == "Lion")
            }
        })
    }

    AppTheme {
        Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            ChipDropDown(chipItems = items1, onSelected = { })
            ChipDropDown(chipItems = items2, onSelected = { })
        }
    }
}
