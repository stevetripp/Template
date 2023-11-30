package com.example.template.ux.stickyheaders

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class StickyHeadersUiState(
    val lazyColumnItemsFlow: StateFlow<List<LazyColumnItem>> = MutableStateFlow(emptyList()),
)