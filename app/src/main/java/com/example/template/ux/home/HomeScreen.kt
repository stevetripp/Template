package com.example.template.ux.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.MainAppScaffoldWithNavBar
import com.example.template.ux.main.MainUiState
import com.example.template.ux.main.MainViewModel
import com.example.template.ux.main.Screen
import com.example.template.ux.main.getSharedMainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.ui.compose.navigation.HandleNavigation

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel(), mainViewModel: MainViewModel = getSharedMainViewModel()) {
    HomeContent(viewModel.uiState, mainViewModel.uiState)
    HandleNavigation(viewModelNav = viewModel, navController = navController)
}

@Composable
fun HomeContent(uiState: HomeScreenUiState, mainUiState: MainUiState) {
    val screens by uiState.screensFlow.collectAsStateWithLifecycle()
//    val statusBarHeight = with(LocalDensity.current) {
//        WindowInsets.systemBars.getTop(this).toDp()
//    }

    val appBarMenu: @Composable RowScope.() -> Unit = {
        IconButton(mainUiState.onSettingsClicked) { Icon(Icons.Default.Settings, contentDescription = null) }
    }

    MainAppScaffoldWithNavBar(
//        modifier = Modifier.padding(top = statusBarHeight),
        title = Screen.HOME.title,
        navigationIconVisible = false,
        actions = { appBarMenu() }
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 48.dp),
        ) {
            items(screens) { screen ->
                Card(modifier = Modifier.height(60.dp), onClick = { uiState.onItemClicked(screen) }) {
                    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                        Text(modifier = Modifier.fillMaxWidth(), text = screen.title, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun HomeContentPreview() {
    AppTheme { HomeContent(HomeScreenUiState(MutableStateFlow(Screen.entries)), MainUiState()) }
}