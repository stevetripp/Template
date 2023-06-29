package com.example.template.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun FilterChipWrapper(
    selected: Boolean,
    highlighted: Boolean,
    title: String,
    defaultColors: SelectableChipColors = FilterChipDefaults.filterChipColors(containerColor = AppTheme.colors.surface),
    highlightColors: SelectableChipColors = FilterChipDefaults.filterChipColors(containerColor = AppTheme.colors.secondary),
    selectedColors: SelectableChipColors = FilterChipDefaults.filterChipColors(),
    onClick: () -> Unit,
) {
    val colors = when {
        selected -> selectedColors
        highlighted -> highlightColors
        else -> defaultColors
    }
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(title) },
        trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, title) },
        colors = colors,
        elevation = FilterChipDefaults.filterChipElevation(elevation = 2.dp),
        border = FilterChipDefaults.filterChipBorder(borderColor = Color.Transparent),
    )
}


@PreviewDefault
@Composable
private fun FilterChipItemPreview() {
    AppTheme {
        Surface {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FilterChipWrapper(selected = false, highlighted = false, title = "!selected && !highlighted", onClick = {})
                FilterChipWrapper(selected = false, highlighted = true, title = "!selected && highlighted", onClick = {})
                FilterChipWrapper(selected = true, highlighted = false, title = "selected && !highlighted", onClick = {})
            }
        }
    }
}