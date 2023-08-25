package com.example.template.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RefreshBox(
    isRefreshingFlow: StateFlow<Boolean>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val isRefreshing by isRefreshingFlow.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)
    Box(
        modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        content.invoke(this)
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState
        )
    }
}

@PreviewDefault
@Composable
private fun RefreshBoxPreview() {
    AppTheme {
        RefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshingFlow = MutableStateFlow(true),
            onRefresh = {}
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Content")
            }
        }
    }
}