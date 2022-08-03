package com.example.template.ux.reorderablelist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ux.main.Screen
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ReorderableListScreen(navController: NavController) {
    ReorderableListContent(navController::popBackStack)
}

@Composable
fun ReorderableListContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.REORDERABLE_LIST.title, onBack = onBack) }) { paddingValues ->
        val items = List(100) { "Item $it" }
        var rememberedList by remember(items) { mutableStateOf(items) }
        val state = rememberReorderableLazyListState(onMove = { from, to ->
            rememberedList = rememberedList.toMutableList().apply { add(to.index, removeAt(from.index)) }
        })
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .reorderable(state)
                .detectReorderAfterLongPress(state) // remove to prevent selection
        ) {
            items(rememberedList, { it }) { item ->
                ReorderableItem(state, key = item) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                    Column(
                        modifier = Modifier
                            .shadow(elevation.value)
                            .background(MaterialTheme.colors.surface)
                    ) {
                        ListItem(text = { Text(item) })
                    }
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun ReorderableListContentPreview() {
    AppTheme { ReorderableListContent() }
}