package com.example.template.ux.synchronizescrolling

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewPhoneOrientations
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.composable.FilterTextField
import com.example.template.ui.composable.SynchronizeScrolling
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.example.template.ux.search.SearchViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SynchronizeScrollingScreen(navController: NavController, viewModel: SynchronizeScrollingViewModel = hiltViewModel()) {
    SynchronizeScrollingContent(viewModel.uiState, navController::popBackStack)
}

@Composable
private fun SynchronizeScrollingContent(uiState: SynchronizeScrollingUiState, onBack: () -> Unit = {}) {
    val names by uiState.namesFlow.collectAsStateWithLifecycle()
    val query by uiState.queryFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopAppBar(title = Screen.SYNCHRONIZE_SCROLLING.title, onBack = onBack) }
    ) { paddingValues ->
        SynchronizeScrolling(modifier = Modifier.padding(paddingValues),
            pinSyncedContent = query.isNotBlank(),
            syncedContent = { syncModifier ->
                FilterTextField(
                    modifier = syncModifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    query = query,
                    placeholder = "Placeholder",
                    onQueryChange = uiState.onQueryChange
                )
            }) { contentPadding ->
            LazyColumn(contentPadding = contentPadding) {
                items(names) { name ->
                    ListItem(headlineContent = { Text(text = name) })
                }
            }
        }
    }
}

@PreviewPhoneOrientations
@Composable
private fun Preview() {
    val listFlow = MutableStateFlow(SearchViewModel.randomNames)
    AppTheme { SynchronizeScrollingContent(SynchronizeScrollingUiState(namesFlow = listFlow)) }
}