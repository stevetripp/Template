package com.example.template.ux.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.MainAppScaffoldWithNavBar
import com.example.template.ux.main.Screen
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    HomeContent(viewModel.uiState)
    HandleNavigation(viewModelNav = viewModel, navController = navController)
}

@Composable
fun HomeContent(uiState: HomeScreenUiState) {
    val scrollState = rememberScrollState()
    MainAppScaffoldWithNavBar(title = Screen.HOME.title, navigationIconVisible = false) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Screen.entries.filterNot { it == Screen.HOME }.forEach { destination ->
                TextButton(
                    onClick = { uiState.onItemClicked(destination) },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.textButtonColors(containerColor = AppTheme.colors.surface)
                ) {
                    Text(destination.title)
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun HomeContentPreview() {
    AppTheme { HomeContent(HomeScreenUiState()) }
}