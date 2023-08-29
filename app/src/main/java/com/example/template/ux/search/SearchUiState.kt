package com.example.template.ux.search

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SearchUiState(
    val filteredListFlow: StateFlow<List<String>> = MutableStateFlow(emptyList()),
    val suggestionListFlow: StateFlow<List<String>> = MutableStateFlow(emptyList()),
    val queryTextFlow: StateFlow<String> = MutableStateFlow(""),
    val onQueryChange: (String) -> Unit = {},
    val onSearch: (String) -> Unit = {}
)