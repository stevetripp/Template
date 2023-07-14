package com.example.template.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun FilterChipWrapper(
    selected: Boolean,
    title: String,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(title) },
        trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, title) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AppTheme.colors.secondary,
            selectedLabelColor = AppTheme.colors.onSecondary,
            selectedTrailingIconColor = AppTheme.colors.onSecondary
        ),
    )
}

@PreviewDefault
@Composable
private fun FilterChipItemPreview() {
    AppTheme {
        Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FilterChipWrapper(selected = false, title = "!selected") {}
            FilterChipWrapper(selected = true, title = "selected") {}
        }
    }
}