package com.example.template.ux.pullrefresh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.RefreshBox
import com.example.template.ux.main.Screen

@Composable
fun PullRefreshScreen(navController: NavHostController, viewModel: PullRefreshViewModel = hiltViewModel()) {
    PullRefreshContent(viewModel.uiState, onBack = navController::popBackStack)
}

@Composable
fun PullRefreshContent(uiState: PullRefreshUiState, onBack: () -> Unit = {}) {
    val listItems by uiState.listItemsFlow.collectAsState()
    Scaffold(
        topBar = { AppTopAppBar(title = Screen.PULL_REFRESH.title, onBack = onBack) }) {
        RefreshBox(isRefreshingFlow = uiState.isRefreshingFlow, onRefresh = uiState.onRefresh) {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                items(listItems) { item ->
                    ListItem(text = { Text(text = item) })
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { PullRefreshContent(PullRefreshUiState()) }
}