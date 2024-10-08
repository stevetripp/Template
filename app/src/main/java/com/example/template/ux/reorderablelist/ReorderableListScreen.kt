package com.example.template.ux.reorderablelist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ReorderableListScreen(navController: NavController, viewModel: ReorderableListViewModel = hiltViewModel()) {
    ReorderableListContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun ReorderableListContent(uiState: ReorderableListUiState, onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.REORDERABLE_LIST.title, onBack = onBack) }) { paddingValues ->
        val list by uiState.listFlow.collectAsStateWithLifecycle()
        val modifier = Modifier.padding(paddingValues)


        val reorderableItem: @Composable (ReorderableState<*>, ReorderableItemData) -> Unit = { state, item ->
            ReorderableItem(state = state, key = item.id) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "animateDpAsState")
                Box(
                    modifier = Modifier
                        .shadow(elevation)
                        .detectReorderAfterLongPress(state) // remove to prevent selection
                ) {
                    ListItem(headlineContent = { Text(item.value) })
                }
            }
        }

        if (AppTheme.isLandscape) {
            val state = rememberReorderableLazyGridState(uiState.onMove, canDragOver = uiState.canDragOver)
            LazyVerticalGrid(
                state = state.gridState,
                modifier = modifier
                    .reorderable(state),
                columns = GridCells.Fixed(2)
            ) {
                items(items = list, key = { it.id }) { item -> reorderableItem(state, item) }
            }
        } else {
            val state = rememberReorderableLazyListState(onMove = uiState.onMove, canDragOver = uiState.canDragOver)
            LazyColumn(
                state = state.listState,
                modifier = modifier
                    .reorderable(state)
            ) {
                items(items = list, key = { it.id }) { item -> reorderableItem(state, item) }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun ReorderableListContentPreview() {
    AppTheme { ReorderableListContent(ReorderableListUiState()) }
}