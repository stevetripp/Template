package com.example.template.ux.pullrefresh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.RefreshBox
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.ui.ext.popBackStackOrFinishActivity

@Composable
fun PullRefreshScreen(navController: NavHostController, viewModel: PullRefreshViewModel = hiltViewModel()) {
    val context = LocalContext.current
    PullRefreshContent(viewModel.uiState) {
        // This is needed for deep linking to close the app when tapping back
        navController.popBackStackOrFinishActivity(context)
    }
}

@Composable
fun PullRefreshContent(uiState: PullRefreshUiState, onBack: () -> Unit = {}) {
    val listItems by uiState.listItemsFlow.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { AppTopAppBar(title = Screen.PULL_REFRESH.title, onBack = onBack) }
    ) { paddingValues ->
        RefreshBox(
            modifier = Modifier
                .padding(paddingValues),
            isRefreshingFlow = uiState.isRefreshingFlow,
            onRefresh = uiState.onRefresh
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(listItems) { item ->
                    ListItem(headlineContent = { Text(text = item) })
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { PullRefreshContent(PullRefreshUiState(isRefreshingFlow = MutableStateFlow(true))) }
}