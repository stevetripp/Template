package com.example.template.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
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
fun ChipModalBottomSheet(chipItems: List<ChipItem>, onItemSelected: (ChipItem.Selectable) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val firstSelectedChipItem = chipItems.find { it is ChipItem.Selectable && it.checked } as? ChipItem.Selectable
    val selectedCount = chipItems.count { it is ChipItem.Selectable && it.checked }
    val chipText = firstSelectedChipItem?.let { chipItem ->
        when (chipItem) {
            is ChipItem.Category -> chipItem.text
            else -> chipItem.text + if (selectedCount > 1) "+$selectedCount" else ""
        }
    }.orEmpty()

    FilterChipWrapper(
        selected = firstSelectedChipItem !is ChipItem.Category,
        title = chipText
    ) { showBottomSheet = !showBottomSheet }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
            onDismissRequest = { showBottomSheet = false }
        ) {
            ChipModalBottomSheetContent(
                chipItems = chipItems,
                onItemSelected = onItemSelected
            )
        }
    }
}

@PreviewDefault
@Composable
private fun ChipModalBottomSheetPreview(
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
            ChipModalBottomSheet(chipItems = items1) {}
            ChipModalBottomSheet(chipItems = items2) {}
        }
    }
}