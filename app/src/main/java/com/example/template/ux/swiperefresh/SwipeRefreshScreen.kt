package com.example.template.ux.swiperefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

@Composable
fun SwipeRefreshScreen(navController: NavHostController, viewModel: SwipeRefreshViewModel = hiltViewModel()) {
    SwipeRefreshContent(viewModel.uiState, onBack = navController::popBackStack)
}

@Composable
fun SwipeRefreshContent(uiState: SwipeRefreshUiState, onBack: () -> Unit = {}) {
    val listItems by uiState.listItemsFlow.collectAsState()
    val isRefreshing by uiState.isRefreshingFlow.collectAsState()
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = uiState.onRefresh)
    Scaffold(
        topBar = { AppTopAppBar(title = Screen.SWIPE_REFRESH.title, onBack = onBack) }) {
        Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(modifier = Modifier
                .padding(it)
                .fillMaxSize()) {
                items(listItems) { item ->
                    ListItem(text = { Text(text = item) })
                }
            }
            PullRefreshIndicator(modifier = Modifier.align(Alignment.TopCenter), refreshing = isRefreshing, state = pullRefreshState)
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { SwipeRefreshContent(SwipeRefreshUiState()) }
}