package com.example.template.ux.filtertextfield

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class FilterTextUiState(
    val namesFlow: StateFlow<List<String>> = MutableStateFlow(emptyList()),
    val queryFlow: StateFlow<String> = MutableStateFlow(""),

    val onQueryChange: (String) -> Unit = {}
)