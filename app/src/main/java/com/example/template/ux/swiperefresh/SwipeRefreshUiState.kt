package com.example.template.ux.swiperefresh

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SwipeRefreshUiState(
    val listItemsFlow: StateFlow<List<String>> = MutableStateFlow(emptyList()),
    val isRefreshingFlow: StateFlow<Boolean> = MutableStateFlow(false),
    val onRefresh: () -> Unit = {}
)