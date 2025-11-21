package com.example.template.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Clickable(
    text: String,
    secondaryText: String? = null,
    icon: @Composable (() -> Unit)? = null,
    onClickBody: (() -> Unit)? = null
) {
    ListItem(
        modifier = Modifier.Companion
            .clickable { onClickBody?.invoke() },
        leadingContent = icon,
        headlineContent = {
            Text(text)
        },
        supportingContent = if (!secondaryText.isNullOrBlank()) {
            { Text(secondaryText) }
        } else {
            null
        }
    )
}
