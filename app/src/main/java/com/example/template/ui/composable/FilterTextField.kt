package com.example.template.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun FilterTextField(query: String, placeholder: String, modifier: Modifier = Modifier, onQueryChange: (String) -> Unit) {
    val density = LocalDensity.current
    var textSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    val roundedCornerShape by remember(textSize) { mutableStateOf(with(density) { RoundedCornerShape(textSize.height.toDp()) }) }

    SearchBarDefaults.InputField(
        modifier = modifier
            .clip(roundedCornerShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .onSizeChanged { textSize = it },
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { },
        expanded = false,
        onExpandedChange = {},
        placeholder = { Text(text = placeholder) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.clickable { onQueryChange("") })
        },
    )
}

@PreviewDefault
@Composable
private fun Preview() {
    var query by remember { mutableStateOf("") }
    AppTheme { /*Surface { */FilterTextField(query = query, placeholder = "Placeholder", modifier = Modifier.fillMaxWidth(), onQueryChange = { query = it }) }// }
}