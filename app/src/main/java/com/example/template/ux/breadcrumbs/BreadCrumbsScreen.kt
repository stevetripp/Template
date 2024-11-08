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
import kotlinx.coroutines.flow.StateFlow
import org.lds.mobile.ui.compose.navigation.HandleNavigation

data class BreadCrumbsUiState(
    val titleFlow: StateFlow<String> = MutableStateFlow("Title"),
    val onNavigate: () -> Unit = {},
)

@Composable
fun BreadCrumbsScreen(navController: NavController, viewModel: BreadCrumbsViewModel = hiltViewModel()) {
    BreadCrumbsContent(viewModel.uiState, navController::popBackStack)
    HandleNavigation(viewModel, navController)
}

@Composable
fun BreadCrumbsContent(uiState: BreadCrumbsUiState, onBack: () -> Unit = {}) {
    val title by uiState.titleFlow.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopAppBar(title = title, onBack = onBack) }) { paddingValues ->
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
    AppTheme { BreadCrumbsContent(BreadCrumbsUiState(titleFlow = MutableStateFlow(Screen.BREAD_CRUMBS_SCREEN.title))) }
}