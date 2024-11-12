package com.example.template.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.breadcrumbs.BreadcrumbRoute
import com.example.template.ux.breadcrumbs.BreadcrumbsRoute

@Composable
fun AppTopAppBar(
    title: String,
    navigationImage: ImageVector? = Icons.AutoMirrored.Default.ArrowBack,
    onBack: () -> Unit = {}
) {
    AppTopAppBar(
        title = title,
        navigationImage = navigationImage,
        breadcrumbRoutes = emptyList(),
        onBreadCrumbClicked = {},
        onBack = onBack
    )
}

@Composable
fun AppTopAppBar(
    title: String,
    navigationImage: ImageVector? = Icons.AutoMirrored.Default.ArrowBack,
    breadcrumbRoutes: List<BreadcrumbRoute>,
    onBreadCrumbClicked: (BreadcrumbRoute) -> Unit,
    onBack: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Column(
                modifier = Modifier
                    .then(if (breadcrumbRoutes.isNotEmpty()) Modifier.clickable { expanded = true } else Modifier)
            ) {
                Text(text = title)
                breadcrumbRoutes.lastOrNull()?.let {
                    Row {
                        Text(text = it.breadcrumbTitle, style = MaterialTheme.typography.titleSmall)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                breadcrumbRoutes.reversed().forEach { breadCrumb ->
                    DropdownMenuItem(
                        text = { Text(breadCrumb.breadcrumbTitle) },
                        onClick = {
                            onBreadCrumbClicked(breadCrumb)
                            expanded = false
                        },
                    )
                }
            }
        },
        navigationIcon = navigationImage?.let {
            { IconButton(onClick = onBack) { Icon(it, "Back") } }
        } ?: { }
    )
}

@PreviewDefault
@Composable
private fun Preview(
    @PreviewParameter(BreadCrumbPreviewParameter::class) breadCrumbs: List<BreadcrumbRoute>
) {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopAppBar(title = "Title", breadcrumbRoutes = breadCrumbs, onBreadCrumbClicked = {})
        }
    }
}

private class BreadCrumbPreviewParameter : PreviewParameterProvider<List<BreadcrumbRoute>> {
    override val values: Sequence<List<BreadcrumbRoute>>
        get() = sequenceOf(
            emptyList(),
            (1..3).map { BreadcrumbsRoute("Subtitle $it") }
        )
}