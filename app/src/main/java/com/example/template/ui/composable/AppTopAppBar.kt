package com.example.template.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun AppTopAppBar(title: String, navigationImage: ImageVector? = Icons.Default.ArrowBack, onBack: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = navigationImage?.let {
            { IconButton(onClick = onBack) { Icon(it, "Back") } }
        } ?: { }
    )
}

@PreviewDefault
@Composable
private fun AppTopAppBarPreview() {
    AppTheme { AppTopAppBar("Title") }
}