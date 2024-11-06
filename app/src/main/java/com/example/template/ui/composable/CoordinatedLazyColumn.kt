package com.example.template.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun CoordinatedLazyColumn(
    pinCoordinatedContent: Boolean,
    modifier: Modifier = Modifier,
    coordinatedContent: @Composable (Modifier) -> Unit,
    content: LazyListScope.() -> Unit
) {
    val density = LocalDensity.current

    var coordinatedContentSize by remember { mutableStateOf(IntSize(0, 0)) }
    val coordinatedContentHeightDp by remember(coordinatedContentSize) { mutableStateOf(with(density) { coordinatedContentSize.height.toDp() }) }
    var coordinatedContentHeightOffsetPx by remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember(coordinatedContentSize) {
        object : NestedScrollConnection {
            val coordinatedContentHeightPx = coordinatedContentSize.height.toFloat()

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (pinCoordinatedContent) {
                    val delta = available.y
                    val newOffset = coordinatedContentHeightOffsetPx + delta
                    coordinatedContentHeightOffsetPx = newOffset.coerceIn(-coordinatedContentHeightPx, 0f)
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
        coordinatedContent(Modifier
            .zIndex(1f)
            .offset { IntOffset(0, coordinatedContentHeightOffsetPx.roundToInt()) }
            .onSizeChanged { coordinatedContentSize = it }
        )
        LazyColumn(contentPadding = PaddingValues(top = coordinatedContentHeightDp)) {
            content()
        }
    }
}