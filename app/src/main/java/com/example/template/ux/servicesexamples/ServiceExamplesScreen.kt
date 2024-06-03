package com.example.template.ux.servicesexamples

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.MainAppScaffoldWithNavBar
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun ServicesExamplesScreen(navController: NavController, viewModel: ServicesExamplesViewModel = hiltViewModel()) {
    ServicesExamplesContent(viewModel.uiState)
    HandleNavigation(viewModelNav = viewModel, navController = navController)
}

@Composable
fun ServicesExamplesContent(uiState: ServicesExamplesScreenUiState) {
    val screens by uiState.screensFlow.collectAsStateWithLifecycle()
    MainAppScaffoldWithNavBar(title = Screen.SERVICE_EXAMPLES.title, navigationIconVisible = false) {
        LazyColumn {
            items(screens) { destination ->
                TextButton(
                    onClick = { uiState.onItemClicked(destination) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillParentMaxWidth(),
                ) {
                    Text(destination.title)
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { ServicesExamplesContent(ServicesExamplesScreenUiState(MutableStateFlow(Screen.entries))) }
}