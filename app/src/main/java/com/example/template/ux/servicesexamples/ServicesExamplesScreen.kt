package com.example.template.ux.servicesexamples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
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
fun ServicesExamplesScreen(navigator: Navigation3Navigator, viewModel: ServicesExamplesViewModel) {
    ServicesExamplesContent(navigator, viewModel.uiState)
    HandleNavigation3(viewModelNavigation = viewModel, navigator = navigator)
}

@Composable
fun ServicesExamplesContent(navigator: Navigation3Navigator, uiState: ServicesExamplesScreenUiState) {
    val screens by uiState.screensFlow.collectAsStateWithLifecycle()
    MainAppScaffoldWithNavBar(
        title = Screen.SERVICE_EXAMPLES.title,
        selectedRoute = navigator.getSelectedTopLevelRoute(),
        onNavBarItemSelected = { navBarItem, reselected -> navigator.navigateTopLevel(navBarItem.route, reselected) },
        navigationIconVisible = false
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(screens) { destination ->
                Card(modifier = Modifier.height(60.dp), onClick = { uiState.onItemClicked(destination) }) {
                    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                        Text(modifier = Modifier.fillMaxWidth(), text = destination.title, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { ServicesExamplesContent(PreviewNavigator(), ServicesExamplesScreenUiState(MutableStateFlow(Screen.entries))) }
}