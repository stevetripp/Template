package com.example.template.ux.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
    MainAppScaffoldWithNavBar(title = Screen.HOME.title, navigationIconVisible = false) {
        LazyColumn {
            items(Screen.entries.filterNot { it == Screen.HOME }) { destination ->
                TextButton(
                    onClick = { uiState.onItemClicked(destination) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillParentMaxWidth(),
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