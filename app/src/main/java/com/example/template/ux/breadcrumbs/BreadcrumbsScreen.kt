package com.example.template.ux.breadcrumbs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun BreadcrumbsScreen(navController: NavController, viewModel: BreadCrumbsViewModel = hiltViewModel()) {
    BreadcrumbsContent(viewModel.uiState, navController::popBackStack)
    HandleNavigation(viewModel, navController)
}

@Composable
fun BreadcrumbsContent(uiState: BreadcrumbsUiState, onBack: () -> Unit = {}) {
    val breadcrumbRoutes by uiState.breadcrumbRoutesFlow.collectAsStateWithLifecycle()
    val title by uiState.titleFlow.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        AppTopAppBar(
            title = title,
            breadcrumbRoutes = breadcrumbRoutes,
            onBreadCrumbClicked = uiState.onBreadCrumbClicked,
            onBack = onBack,
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            TextButton(uiState.onNavigate) { Text(text = "Navigate") }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { BreadcrumbsContent(BreadcrumbsUiState(titleFlow = MutableStateFlow(Screen.BREADCRUMBS_SCREEN.title))) }
}