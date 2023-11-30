package com.example.template.ux.stickyheaders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class StickyHeadersUiState(
    val listItemsFlow: StateFlow<Map<String, List<String>>> = MutableStateFlow(emptyMap()),
)

@Composable
fun StickyHeadersScreen(navController: NavController, viewModel: StickyHeaderViewModel = hiltViewModel()) {
    StickyHeadersContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun StickyHeadersContent(uiState: StickyHeadersUiState, onBack: () -> Unit = {}) {
    val listItems by uiState.listItemsFlow.collectAsStateWithLifecycle()
    Scaffold(topBar = { AppTopAppBar(title = Screen.STICKY_HEADERS.title, onBack = onBack) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            listItems.forEach { (header, list) ->
                stickyHeader {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Cyan), text = header
                    )
                }
                items(list) {
                    Text(modifier = Modifier.fillMaxWidth(), text = it)
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { StickyHeadersContent(StickyHeadersUiState()) }
}