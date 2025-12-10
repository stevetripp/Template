package com.example.template.ux.breadcrumbs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import org.lds.mobile.navigation3.navigator.Navigation3Navigator
import org.lds.mobile.ui.compose.navigation.HandleNavigation3

@Composable
fun BreadcrumbsScreen(navigator: Navigation3Navigator, viewModel: BreadCrumbsViewModel) {
    BreadcrumbsContent(viewModel.uiState, navigator::pop)
    HandleNavigation3(viewModel, navigator)
}

@Composable
fun BreadcrumbsContent(uiState: BreadcrumbsUiState, onBack: () -> Unit = {}) {
    val breadcrumbRoutes = uiState.breadcrumbRoutes
    val title = breadcrumbRoutes.lastOrNull()?.title.orEmpty()
    val backstack = breadcrumbRoutes.dropLast(1)

    Scaffold(topBar = {
        AppTopAppBar(
            title = title,
            breadcrumbRoutes = backstack,
            onBreadCrumbClicked = uiState.onBreadCrumbClicked,
            onBack = onBack,
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            TextButton({ uiState.onNavigate() }) { Text(text = "Navigate") }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    val sampleRoutes = listOf(
        BreadcrumbsRoute(title = "Home"),
        BreadcrumbsRoute(title = "Section"),
        BreadcrumbsRoute(title = "Details")
    )
    AppTheme { BreadcrumbsContent(BreadcrumbsUiState()) }
}