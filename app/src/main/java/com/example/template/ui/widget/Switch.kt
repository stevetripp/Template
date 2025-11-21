package com.example.template.ui.widget

import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Switch(
    text: String,
    currentCheckedValueFlow: StateFlow<Boolean>,
    modifier: Modifier = Modifier,
    secondaryText: String? = null,
    icon: @Composable (() -> Unit)? = null,
    onClickBody: ((Boolean) -> Unit)? = null
) {
    val currentValueChecked by currentCheckedValueFlow.collectAsStateWithLifecycle()

    ListItem(
        modifier = modifier
            .toggleable(currentValueChecked, onValueChange = { onClickBody?.invoke(it) }, role = Role.Companion.Switch),
        leadingContent = icon,
        headlineContent = {
            Text(text)
        },
        supportingContent = if (!secondaryText.isNullOrBlank()) {
            { Text(secondaryText) }
        } else {
            null
        },
        trailingContent = {
            Switch(
                checked = currentValueChecked,
                onCheckedChange = onClickBody
            )
        }
    )
}