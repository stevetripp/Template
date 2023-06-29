package com.example.template.ui.widget

import android.util.Log
import androidx.compose.material.Surface
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun ChipModalBottomSheet(chipItems: List<ChipItem>, onItemsChanged: (List<ChipItem>) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val firstSelectedChipItem = chipItems.find { it is ChipItem.Selectable && it.checked } as? ChipItem.Selectable
    val selectedCount = chipItems.count { it is ChipItem.Selectable && it.checked }
    val chipText = firstSelectedChipItem?.let { chipItem ->
        when (chipItem) {
            is ChipItem.Category -> chipItem.text
            else -> chipItem.text + if (selectedCount > 1) "+$selectedCount" else ""
        }
    }.orEmpty()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    FilterChipWrapper(
        selected = showBottomSheet,
        highlighted = firstSelectedChipItem !is ChipItem.Category,
        title = chipText,
        onClick = { showBottomSheet = !showBottomSheet })

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { showBottomSheet = false }
        ) {
            ChipModalBottomSheetContent(
                chipItems = chipItems,
                onHide = {
                    scope.launch {
                        bottomSheetState.hide()
                        showBottomSheet = false
                    }
                },
                onItemsChanged = onItemsChanged
            )
        }
    }
}

@PreviewDefault
@Composable
private fun ChipModalBottomSheetPreview(
    @PreviewParameter(ChipItemsPreviewParameterProvider::class) previewItems: List<ChipItem>
) {
    var items by remember { mutableStateOf(previewItems) }

    AppTheme {
        Surface {
            ChipModalBottomSheet(chipItems = items, onItemsChanged = { changedItems ->
                Log.i("SMT", "OnChipItem Clicked")
                items = changedItems
            })
        }
    }
}