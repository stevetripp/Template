package com.example.template.ux.pullrefresh

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PullRefreshUiState(
    val closeOnBack: Boolean = false,
    val listItemsFlow: StateFlow<List<String>> = MutableStateFlow(emptyList()),
    val isRefreshingFlow: StateFlow<Boolean> = MutableStateFlow(false),
    val onRefresh: () -> Unit = {}
)