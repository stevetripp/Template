package com.example.template.ux.flippable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.FlippableController
import org.koin.compose.viewmodel.koinViewModel
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun FlippableGridScreen(
    navigator: Navigation3Navigator,
    viewModel: FlippableGridViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FlippableGridContent(
        uiState = uiState,
        onBack = navigator::pop
    )
}

@Composable
fun FlippableGridContent(
    uiState: FlippableGridUiState,
    onBack: () -> Unit = {}
) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.FLIPPABLE_GRID.title, onBack = onBack) }) { paddingValues ->
        val cellCoordinates = remember { mutableStateMapOf<Int, LayoutCoordinates>() }
        val controllers = remember { List(100) { FlippableController() } }
        var parentCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        fun triggerFlip(index: Int) {
            val controller = controllers[index]
            uiState.triggerFlip(index)
            controller.flip()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .onGloballyPositioned { parentCoordinates = it }
                .pointerInput(uiState.isXMode) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val startPos = down.position
                        var currentFlippedList = listOf<Int>()
                        val startCellIndex = cellCoordinates.entries.find { (_, coordinates) ->
                            if (coordinates.isAttached && parentCoordinates != null) {
                                val bounds = parentCoordinates!!.localBoundingBoxOf(coordinates)
                                bounds.contains(startPos)
                            } else {
                                false
                            }
                        }?.key

                        var targetState: Boolean? = null

                        if (startCellIndex != null) {
                            currentFlippedList = listOf(startCellIndex)
                            targetState = uiState.cellIsFlipped[startCellIndex]
                            triggerFlip(startCellIndex)
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

                                if (cellIndex != null && cellIndex !in currentFlippedList) {
                                    if (uiState.cellIsFlipped[cellIndex] == targetState) {
                                        var canFlip = true
                                        if (currentFlippedList.size >= 2) {
                                            val first = currentFlippedList[0]
                                            val second = currentFlippedList[1]
                                            val r1 = first / 10
                                            val c1 = first % 10
                                            val r2 = second / 10
                                            val c2 = second % 10

                                            val isAdjacent = (r1 == r2 && kotlin.math.abs(c1 - c2) == 1) ||
                                                             (c1 == c2 && kotlin.math.abs(r1 - r2) == 1)

                                            if (isAdjacent) {
                                                val cellRow = cellIndex / 10
                                                val cellCol = cellIndex % 10
                                                if (r1 == r2) {
                                                    if (cellRow != r1) {
                                                        canFlip = false
                                                    }
                                                } else if (c1 == c2) {
                                                    if (cellCol != c1) {
                                                        canFlip = false
                                                    }
                                                }
                                            }
                                        }

                                        if (canFlip) {
                                            currentFlippedList = currentFlippedList + cellIndex
                                            triggerFlip(cellIndex)
                                        }
                                    }
                                }

                                change.consume()
                            }
                        }
                    }
                }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (uiState.isXMode) "Displaying 'X'" else "Displaying Solid Square",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Switch(
                        checked = uiState.isXMode,
                        onCheckedChange = uiState.onXModeChanged
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(10),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(100) { index ->
                        val flipController = controllers[index]

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
                                        shape = RectangleShape,
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize())
                                    }
                                },
                                backSide = {
                                    val backColor = if (uiState.cellSymbols[index] == CellSymbol.X) {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.primaryContainer
                                    }

                                    Card(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .aspectRatio(1f),
                                        shape = RectangleShape,
                                        colors = CardDefaults.cardColors(containerColor = backColor)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            when (uiState.cellSymbols[index]) {
                                                CellSymbol.X -> {
                                                    val lineColor = MaterialTheme.colorScheme.onSecondaryContainer
                                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                                        val strokeWidthPx = 2.dp.toPx()
                                                        drawLine(
                                                            color = lineColor,
                                                            start = Offset(0f, 0f),
                                                            end = Offset(size.width, size.height),
                                                            strokeWidth = strokeWidthPx
                                                        )
                                                        drawLine(
                                                            color = lineColor,
                                                            start = Offset(size.width, 0f),
                                                            end = Offset(0f, size.height),
                                                            strokeWidth = strokeWidthPx
                                                        )
                                                    }
                                                }
                                                CellSymbol.SQUARE -> {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .background(
                                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                                shape = RectangleShape
                                                            )
                                                    )
                                                }
                                                CellSymbol.NONE -> {
                                                    Box(modifier = Modifier.fillMaxSize())
                                                }
                                            }
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
}

@PreviewDefault
@Composable
private fun FlippableGridContentPreview() {
    AppTheme { FlippableGridContent(uiState = FlippableGridUiState()) }
}
