package com.example.template.ux.reorderablelist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

@Composable
fun ReorderableListScreen(navController: NavController, viewModel: ReorderableListViewModel = hiltViewModel()) {
    ReorderableListContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun ReorderableListContent(uiState: ReorderableListUiState, onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.REORDERABLE_LIST.title, onBack = onBack) }) { paddingValues ->
        val list by uiState.listFlow.collectAsStateWithLifecycle()
        val modifier = Modifier.padding(paddingValues)

        @Composable
        fun DraggableItemContent(value: String, isDragging: Boolean) {
            val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "animateDpAsState")
            val color by animateColorAsState(if (isDragging) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background)
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation)
                    .background(color),
                headlineContent = { Text(value) }
            )
        }

        if (AppTheme.isLandscape) {
            val gridState = rememberLazyGridState()
            val dragDropState = rememberGridDragDropState(gridState, uiState.onMove)
            LazyVerticalGrid(
                modifier = modifier.dragContainer(dragDropState),
                state = gridState,
                columns = GridCells.Fixed(3)
            ) {
                itemsIndexed(items = list, key = { _, item -> item.id }) { index, item ->
                    DraggableItem(dragDropState, index) { isDragging -> DraggableItemContent(value = item.value, isDragging = isDragging) }
                }
            }
        } else {
            val lazyListState = rememberLazyListState()
            val dragDropState = rememberDragDropState(lazyListState, uiState.onMove)
            LazyColumn(
                modifier = modifier.dragContainer(dragDropState),
                state = lazyListState
            ) {
                itemsIndexed(items = list, key = { _, item -> item.id }) { index, item ->
                    DraggableItem(dragDropState, index) { isDragging -> DraggableItemContent(value = item.value, isDragging = isDragging) }
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun ReorderableListContentPreview() {
    AppTheme { ReorderableListContent(ReorderableListUiState()) }
}