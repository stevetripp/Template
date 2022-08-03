package com.example.template.ui.composable

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

@Composable
fun AppTopAppBar(title: String, navigationImage: ImageVector? = Icons.Default.ArrowBack, onBack: () -> Unit = {}) {
    val navigationIcon: @Composable (() -> Unit)? = navigationImage?.let {
        { IconButton(onClick = onBack) { Icon(it, "Back") } }
    }

    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = navigationIcon
    )
}

@PreviewDefault
@Composable
private fun AppTopAppBarPreview() {
    AppTheme { AppTopAppBar("Title") }
}