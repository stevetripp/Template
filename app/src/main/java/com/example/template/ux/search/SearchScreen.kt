package com.example.template.ux.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    SearchContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun SearchContent(uiState: SearchUiState, onBack: () -> Unit = {}) {
    var active by rememberSaveable { mutableStateOf(false) }
    val query by uiState.queryTextFlow.collectAsStateWithLifecycle()
    val filteredList by uiState.filteredListFlow.collectAsStateWithLifecycle()
    val suggestionList by uiState.suggestionListFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = if (active) {
            {}
        } else {
            { AppTopAppBar(title = Screen.SEARCH.title, onBack = onBack) }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (!active) Modifier.padding(horizontal = 16.dp) else Modifier),
                query = query,
                onQueryChange = uiState.onQueryChanged,
                onSearch = {
                    uiState.onSearch(it)
                    active = false
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Hinted search text") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.clickable {
                        if (query.isNotBlank()) {
                            uiState.onQueryChanged("")
                        } else {
                            active = false
                        }
                    })
                },
            ) {
                suggestionList.forEach {
                    ListItem(
                        headlineContent = { Text(text = it) },
                        leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                        modifier = Modifier
                            .clickable { uiState.onQueryChanged(it) }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }

            LazyColumn {
                items(filteredList) { item ->
                    ListItem(
                        headlineContent = { Text(text = item) },
                        Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}