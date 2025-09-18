package com.example.template.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun ChipModalBottomSheetContent(chipItems: List<ChipItem>, onItemSelected: (ChipItem.Selectable) -> Unit) {
    val categoryChipItem = chipItems.find { it is ChipItem.Category } as? ChipItem.Category

    Column(modifier = Modifier.fillMaxWidth()) {
        categoryChipItem?.let { TopAppBar(title = { Text(text = it.text) }) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            chipItems.forEach { chipItem ->
                when (chipItem) {
                    ChipItem.Divider -> HorizontalDivider()
                    is ChipItem.Selectable -> chipItem.ShowListItem(onClicked = { onItemSelected(chipItem) })
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
    AppTheme { ChipModalBottomSheetContent(chipItems = items) {} }
}