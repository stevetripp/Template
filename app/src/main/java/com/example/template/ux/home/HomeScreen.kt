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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.PreviewNavigator
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.MainAppScaffoldWithNavBar
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation3.navigator.Navigation3Navigator
import org.lds.mobile.ui.compose.navigation.HandleNavigation3

@Composable
fun HomeScreen(navigator: Navigation3Navigator, viewModel: HomeViewModel) {
    HomeContent(navigator, viewModel.uiState)
    HandleNavigation3(viewModelNavigation = viewModel, navigator = navigator)
}

@Composable
fun HomeContent(navigator: Navigation3Navigator, uiState: HomeScreenUiState) {
    val screens by uiState.screensFlow.collectAsStateWithLifecycle()
//    val statusBarHeight = with(LocalDensity.current) {
//        WindowInsets.systemBars.getTop(this).toDp()
//    }

    val appBarMenu: @Composable RowScope.() -> Unit = {
        IconButton(uiState.onSettingsClicked) { Icon(Icons.Default.Settings, contentDescription = null) }
    }

    MainAppScaffoldWithNavBar(
        navigator = navigator,
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
    AppTheme {
        HomeContent(PreviewNavigator(), HomeScreenUiState(MutableStateFlow(Screen.entries)))
    }
}