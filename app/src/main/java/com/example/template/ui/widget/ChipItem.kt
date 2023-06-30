package com.example.template.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

sealed class ChipItem {

    @Composable
    abstract fun ShowListItem(onClicked: () -> Unit)

    @Composable
    abstract fun ShowDropdownMenuItem(onClicked: () -> Unit)

    open class Selectable(
        val text: String,
        val obj: Any? = null,
        var checked: Boolean = false,
    ) : ChipItem() {
        @Composable
        override fun ShowListItem(onClicked: () -> Unit) {
            ListItem(
                modifier = Modifier.clickable { onClicked() },
                headlineContent = { Text(text = text) },
                trailingContent = { Checkbox(checked = checked, onCheckedChange = null) }
            )
        }

        @Composable
        override fun ShowDropdownMenuItem(onClicked: () -> Unit) {
            DropdownMenuItem(
                text = { Text(text = text) },
                onClick = onClicked,
                leadingIcon = { RadioButton(selected = checked, onClick = null) },
            )
        }
    }

    class Category(
        text: String,
        private val bottomSheetText: String,
        obj: Any? = null,
    ) : Selectable(text, obj, true) {
        @Composable
        override fun ShowListItem(onClicked: () -> Unit) {
            ListItem(
                modifier = Modifier.clickable { onClicked() },
                headlineContent = { Text(text = bottomSheetText) },
                trailingContent = { Checkbox(checked = checked, onCheckedChange = null) }
            )
        }

        @Composable
        override fun ShowDropdownMenuItem(onClicked: () -> Unit) {
            DropdownMenuItem(
                text = { Text(text = bottomSheetText) },
                onClick = onClicked,
                leadingIcon = { RadioButton(selected = checked, onClick = null) },
            )
        }
    }

    object Divider : ChipItem() {
        @Composable
        override fun ShowListItem(onClicked: () -> Unit) = Divider()
        @Composable
        override fun ShowDropdownMenuItem(onClicked: () -> Unit) = Divider()
    }
}

class ChipItemsPreviewParameterProvider : PreviewParameterProvider<List<ChipItem>> {
    override val values: Sequence<List<ChipItem>>
        get() = sequenceOf(
            listOf(
                ChipItem.Category("Animals", "Show All"),
                ChipItem.Divider,
                ChipItem.Selectable("Stallion"),
                ChipItem.Selectable("Mustang"),
                ChipItem.Selectable("Lion"),
                ChipItem.Selectable("Tiger"),
            ),
        )
}