package com.example.template.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun DropdownList(value: String, label: String, options: List<DropdownOption>, modifier: Modifier = Modifier, onValueChanged: (DropdownOption) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        if (MaterialTheme.colors.isLight) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                value = value,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
            )
        } else {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                value = value,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onValueChanged(selectionOption)
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption.listValue)
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun DropDownListPreview() {
    val options = mutableListOf<DropdownOption>()
    (1..5).forEach { options.add(DropdownOption("Option $it*", "Option $it")) }
    var selected by remember { mutableStateOf(DropdownOption("")) }
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            DropdownList(selected.selectedValue, "Option", options) {
                selected = it
            }
        }
    }
}