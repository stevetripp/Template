package com.example.template.ux.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.widget.SynchronizedSearchBar
import com.example.template.ux.main.Screen

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    SearchContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun SearchContent(uiState: SearchUiState, onBack: () -> Unit = {}) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val query by uiState.queryTextFlow.collectAsStateWithLifecycle()
    val filteredList by uiState.filteredListFlow.collectAsStateWithLifecycle()
    val suggestionList by uiState.suggestionListFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { if (!isExpanded) AppTopAppBar(title = Screen.SEARCH.title, onBack = onBack) }
    ) { paddingValues ->

        SynchronizedSearchBar(
            modifier = Modifier.padding(paddingValues),
            query = query,
            scrollable = { modifier ->
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(filteredList) { item ->
                        ListItem(
                            headlineContent = { Text(text = item) },
                            Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            isExpanded = isExpanded,
            onQueryChange = uiState.onQueryChange,
            onSearch = uiState.onSearch,
            onExpandedChanged = { isExpanded = it }
        ) {
            suggestionList.forEach {
                ListItem(
                    headlineContent = { Text(text = it) },
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                    modifier = Modifier
                        .clickable { uiState.onQueryChange(it) }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}