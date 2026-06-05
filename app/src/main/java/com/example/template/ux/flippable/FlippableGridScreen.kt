package com.example.template.ux.flippable

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.FlippableController
import com.wajahatkarim.flippable.rememberFlipController
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun FlippableGridScreen(navigator: Navigation3Navigator) {
    FlippableGridContent(onBack = navigator::pop)
}

@Composable
fun FlippableGridContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.FLIPPABLE_GRID.title, onBack = onBack) }) { paddingValues ->
        val cellCoordinates = remember { mutableStateMapOf<Int, LayoutCoordinates>() }
        val controllers = remember { List(16) { FlippableController() } }
        var parentCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .onGloballyPositioned { parentCoordinates = it }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val startPos = down.position
                        var currentFlipped = setOf<Int>()
                        val startCellIndex = cellCoordinates.entries.find { (_, coordinates) ->
                            if (coordinates.isAttached && parentCoordinates != null) {
                                val bounds = parentCoordinates!!.localBoundingBoxOf(coordinates)
                                bounds.contains(startPos)
                            } else {
                                false
                            }
                        }?.key

                        if (startCellIndex != null) {
                            currentFlipped = setOf(startCellIndex)
                            controllers[startCellIndex].flip()
                        }

                        var dragPointerId = down.id
                        while (true) {
                            val event = awaitPointerEvent()
                            val anyDown = event.changes.any { it.pressed }
                            if (!anyDown) {
                                break
                            }

                            val change = event.changes.find { it.id == dragPointerId } ?: event.changes.firstOrNull()
                            if (change != null) {
                                dragPointerId = change.id
                                val currentPos = change.position
                                val cellIndex = cellCoordinates.entries.find { (_, coordinates) ->
                                    if (coordinates.isAttached && parentCoordinates != null) {
                                        val bounds = parentCoordinates!!.localBoundingBoxOf(coordinates)
                                        bounds.contains(currentPos)
                                    } else {
                                        false
                                    }
                                }?.key

                                if (cellIndex != null && cellIndex !in currentFlipped) {
                                    currentFlipped = currentFlipped + cellIndex
                                    controllers[cellIndex].flip()
                                }

                                change.consume()
                            }
                        }
                    }
                }
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(16) { index ->
                    val flipController = controllers[index]
                    val cellNumber = index + 1

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .onGloballyPositioned { coordinates ->
                                cellCoordinates[index] = coordinates
                            }
                    ) {
                        Flippable(
                            frontSide = {
                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .aspectRatio(1f),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$cellNumber\nFront",
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 2
                                        )
                                    }
                                }
                            },
                            backSide = {
                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .aspectRatio(1f),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$cellNumber\nBack",
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 2
                                        )
                                    }
                                }
                            },
                            flipController = flipController,
                            modifier = Modifier.aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun FlippableGridContentPreview() {
    AppTheme { FlippableGridContent() }
}
