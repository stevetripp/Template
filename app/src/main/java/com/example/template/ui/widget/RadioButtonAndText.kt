package com.example.template.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun RadioButtonAndText(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(45.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(modifier = Modifier.padding(end = 8.dp), selected = selected, onClick = null)
        Text(text = text)
    }
}

@PreviewDefault
@Composable
private fun RadioButtonAndTextPreview() {
    AppTheme {
        Surface {
            Column(modifier = Modifier.fillMaxWidth()) {
                RadioButtonAndText(text = "Radio Button 1", selected = true, onClick = {})
                RadioButtonAndText(text = "Radio Button 2", selected = false, onClick = {})
            }
        }
    }
}