package com.example.template.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun SynchronizedSearchBar(
    query: String,
    scrollable: @Composable (Modifier) -> Unit,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val localDensity = LocalDensity.current
    val scrollGroupHeightPx = remember { with(localDensity) { 72.dp.roundToPx().toFloat() } }
    var scrollGroupOffsetHeightPx by remember { mutableFloatStateOf(0F) }

    val nestedScrollConnection = remember(query) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                scrollGroupOffsetHeightPx = if (query.isBlank()) {
                    val newOffset = scrollGroupOffsetHeightPx + available.y
                    newOffset.coerceIn(-scrollGroupHeightPx, 0F)
                } else {
                    0F
                }
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { translationY = scrollGroupOffsetHeightPx }
                .then(if (!isActive) Modifier.padding(horizontal = 16.dp) else Modifier),
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                onSearch(it)
                onActiveChange(false)
            },
            active = isActive,
            onActiveChange = onActiveChange,
            placeholder = { Text("Hinted search text") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.clickable {
                    if (query.isNotBlank()) {
                        onQueryChange("")
                        onSearch("")
                    } else {
                        onActiveChange(false)
                    }
                })
            },
            content = content,
        )
        scrollable(
            Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = (scrollGroupHeightPx + scrollGroupOffsetHeightPx) }
        )
    }
}