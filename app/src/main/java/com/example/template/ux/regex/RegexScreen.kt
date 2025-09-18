package com.example.template.ux.regex

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun RegexScreen(navController: NavController, viewModel: RegexViewModel = hiltViewModel()) {
    RegexContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun RegexContent(uiState: RegexUiState, onBack: () -> Unit = {}) {
    val text by uiState.textFlow.collectAsStateWithLifecycle()
    val regexText by uiState.regexTextFlow.collectAsStateWithLifecycle()
    val resultText by uiState.resultsTextFlow.collectAsStateWithLifecycle()
    val multilineModeEnabled by uiState.multilineModeEnabledFlow.collectAsStateWithLifecycle()

    Scaffold(topBar = { AppTopAppBar(title = Screen.REGEX.title, onBack = onBack) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = multilineModeEnabled, onCheckedChange = uiState.onToggleMultilineMode)
                    Text(text = "Enable Multiline Mode")
                }
            }
            item {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = uiState.onTextChange,
                    placeholder = { Text(text = "Text") }
                )
            }
            item {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = regexText,
                    onValueChange = uiState.onRegexTextChange,
                    placeholder = { Text(text = "Regular Expression") }
                )
            }
            item { Text(text = resultText) }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    val uiState = RegexUiState(
        textFlow = MutableStateFlow("In turpis. Praesent vestibulum dapibus nibh. Nam adipiscing."),
        regexTextFlow = MutableStateFlow("Vestibulum dapibus nunc ac augue"),
        resultsTextFlow = MutableStateFlow(
            " Curabitur nisi.\n" +
                    "\n" +
                    "In consectetuer turpis ut velit. Nam commodo suscipit quam.\n" +
                    "\n" +
                    "Fusce vel dui. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis " +
                    "porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci."
        ),
    )
    AppTheme { RegexContent(uiState) }
}