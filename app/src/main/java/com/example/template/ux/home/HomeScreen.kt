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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val inputScreens by uiState.inputScreensFlow.collectAsStateWithLifecycle()
    val navigationScreens by uiState.navigationScreensFlow.collectAsStateWithLifecycle()
    val visualScreens by uiState.visualScreensFlow.collectAsStateWithLifecycle()
    val selectedTab by uiState.selectedTabFlow.collectAsStateWithLifecycle()

    val appBarMenu: @Composable RowScope.() -> Unit = {
        IconButton(uiState.onSettingsClicked) { Icon(Icons.Default.Settings, contentDescription = null) }
    }

    val tabTitles = listOf("Input", "Navigation", "Visual")
    val pagerState = rememberPagerState { tabTitles.size }
    var tabIndex by remember { mutableIntStateOf(selectedTab) }

    MainAppScaffoldWithNavBar(
        navigator = navigator,
        title = Screen.HOME.title,
        navigationIconVisible = false,
        actions = { appBarMenu() }
    ) {
        Column {
            PrimaryTabRow(selectedTabIndex = tabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index, onClick = { tabIndex = index }, text = { Text(text = title) })
                }
            }

            HorizontalPager(state = pagerState) { pageIndex ->
                val screensForTab = when (pageIndex) {
                    0 -> inputScreens
                    1 -> navigationScreens
                    else -> visualScreens
                }

                LazyVerticalGrid(
                    modifier = Modifier.padding( 16.dp),
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 48.dp),
                ) {
                    items(screensForTab) { screen ->
                        Card(modifier = Modifier.height(60.dp), onClick = { uiState.onItemClicked(screen) }) {
                            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                                Text(modifier = Modifier.fillMaxWidth(), text = screen.title, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }

            LaunchedEffect(key1 = tabIndex) { pagerState.animateScrollToPage(tabIndex) }
            LaunchedEffect(key1 = pagerState.settledPage) { tabIndex = pagerState.settledPage }
        }
    }
}

@PreviewDefault
@Composable
private fun HomeContentPreview() {
    AppTheme {
        HomeContent(
            PreviewNavigator(),
            HomeScreenUiState(
                inputScreensFlow = MutableStateFlow(Screen.entries.filter { it.title.contains("Input") }),
                navigationScreensFlow = MutableStateFlow(Screen.entries.filter { it.title.contains("Navigation") }),
                visualScreensFlow = MutableStateFlow(Screen.entries.take(5))
            )
        )
    }
}