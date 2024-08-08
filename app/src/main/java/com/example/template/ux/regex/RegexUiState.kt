package com.example.template.ux.regex

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class RegexUiState(
    val textFlow: StateFlow<String> = MutableStateFlow(""),
    val regexTextFlow: StateFlow<String> = MutableStateFlow(""),
    val resultsTextFlow: StateFlow<String> = MutableStateFlow(""),
    val multilineModeEnabledFlow: StateFlow<Boolean> = MutableStateFlow(false),
    val onTextChange: (String) -> Unit = {},
    val onRegexTextChange: (String) -> Unit = {},
    val onToggleMultilineMode: (Boolean) -> Unit = {},
)