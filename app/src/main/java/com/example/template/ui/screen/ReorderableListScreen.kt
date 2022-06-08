package com.example.template.ui.screen

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.template.AppBar
import com.example.template.Nav
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ReorderableListScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) { paddingValues ->
        val data = remember { mutableStateOf(List(100) { "Item $it" }) }
        val state = rememberReorderableLazyListState(onMove = { from, to ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        })
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .reorderable(state)
                .detectReorderAfterLongPress(state)
        ) {
            items(data.value, { it }) { item ->
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
private fun ReorderableListScreenPreview() {
    AppTheme { ReorderableListScreen(Nav.REORDERABLE_LIST, {}) }
}