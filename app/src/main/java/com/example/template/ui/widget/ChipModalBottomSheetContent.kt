package com.example.template.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun ChipModalBottomSheetContent(chipItems: List<ChipItem>, onHide: () -> Unit, onItemsChanged: (List<ChipItem>) -> Unit) {
    val categoryChipItem = chipItems.find { it is ChipItem.Category } as? ChipItem.Category
    val scope = rememberCoroutineScope()

    Scaffold(topBar = categoryChipItem?.let { { TopAppBar(title = { Text(text = it.text) }) } } ?: {}
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(chipItems) { chipItem ->
                when (chipItem) {
                    ChipItem.Divider -> Divider()
                    is ChipItem.Selectable -> {
                        chipItem.ShowListItem {
                            scope.launch {
                                onHide()
                                val selectableChips = chipItems.filterIsInstance<ChipItem.Selectable>()
                                val categoryChip = chipItems.find { it is ChipItem.Category } as? ChipItem.Category

                                when (chipItem) {
                                    is ChipItem.Category -> {
                                        if (!chipItem.checked) {
                                            selectableChips.forEach { it.checked = false }
                                            chipItem.checked = true
                                        }
                                    }
                                    else -> {
                                        Log.i("SMT", "Before: ${chipItem.checked}")
                                        categoryChip?.checked = false
                                        chipItem.checked = !chipItem.checked
                                        Log.i("SMT", "After: ${chipItem.checked}")
                                        if (selectableChips.count { it.checked } == 0) categoryChip?.checked = true
                                    }
                                }

                                onItemsChanged(chipItems.map { it })
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview(
    @PreviewParameter(ChipItemsPreviewParameterProvider::class) items: List<ChipItem>
) {
    AppTheme { ChipModalBottomSheetContent(items, {}, {}) }
}