package com.example.template.ux.edgetoedge

import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

@Composable
fun EdgeToEdgeListItem(text: String, enabled: Boolean, onToggle: (Boolean) -> Unit) {
    ListItem(
        modifier = Modifier.toggleable(enabled, onValueChange = onToggle, role = Role.Switch),
        headlineContent = { Text(text = text) },
        trailingContent = { Switch(checked = enabled, onCheckedChange = onToggle) },
    )
}