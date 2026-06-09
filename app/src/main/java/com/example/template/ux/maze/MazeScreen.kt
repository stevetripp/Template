package com.example.template.ux.maze

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

/**
 * Main entry point Composable for the Maze game screen.
 * Collects state from the view model and delegates rendering.
 *
 * @param navigator The navigation controller for pop operations.
 * @param viewModel The view model managing state.
 */
@Composable
fun MazeScreen(navigator: Navigation3Navigator, viewModel: MazeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MazeUiState.Loading -> MazeLoadingScreen(onBack = navigator::pop)
        is MazeUiState.Ready -> MazeContent(uiState = state, onBack = navigator::pop)
    }
}

/**
 * Composable screen showing a centered progress spinner during maze loading.
 *
 * @param onBack Callback triggered when navigating back.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MazeLoadingScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Neon Maze",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

/**
 * Composable displaying the active gameplay screen for a generated maze.
 * Renders stats, canvas drawing, D-pad button layout, keyboard listeners,
 * and handles completion/victory dialogs.
 *
 * @param uiState The active [MazeUiState.Ready] state.
 * @param onBack Callback triggered when navigating back.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MazeContent(
    uiState: MazeUiState.Ready,
    onBack: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    // Request keyboard focus when the screen is loaded
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Neon Maze",
                onBack = onBack,
                actions = {
                    IconButton(onClick = uiState.onRegenerate) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Regenerate Maze")
                    }
                    IconButton(onClick = uiState.onToggleSolution) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Help, contentDescription = "Toggle Solution")
                    }
                    Box {
                        var showDifficultyMenu by remember { mutableStateOf(false) }
                        TextButton(
                            onClick = { showDifficultyMenu = true },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                        ) {
                            Text(uiState.difficulty.name)
                        }
                        DropdownMenu(
                            expanded = showDifficultyMenu,
                            onDismissRequest = { showDifficultyMenu = false }
                        ) {
                            MazeDifficulty.entries.forEach { diff ->
                                DropdownMenuItem(
                                    text = { Text(diff.title) },
                                    onClick = {
                                        uiState.onDifficultyChange(diff)
                                        showDifficultyMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            StatsPanel(timeElapsed = uiState.timeElapsed, moveCount = uiState.moveCount)
            MazeGridCanvas(uiState = uiState, focusRequester = focusRequester)
            Spacer(modifier = Modifier.height(16.dp))
            DPadControls(onMove = uiState.onMove)
            VictoryDialog(
                timeElapsed = uiState.timeElapsed,
                moveCount = uiState.moveCount,
                isGameCompleted = uiState.isGameCompleted,
                onRegenerate = uiState.onRegenerate,
                onBack = onBack
            )
        }
    }
}

/**
 * Composable rendering the stats panel (timer and move count).
 */
@Composable
private fun StatsPanel(timeElapsed: Long, moveCount: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "TIME", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                val seconds = timeElapsed / 1000.0
                val locale = LocalLocale.current.platformLocale
                Text(
                    text = String.format(locale, "%.1fs", seconds),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(
                modifier = Modifier
                    .height(32.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "MOVES", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(
                    text = moveCount.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Composable rendering the interactive maze Canvas and listening to keyboard movements.
 */
@Composable
private fun MazeGridCanvas(
    uiState: MazeUiState.Ready,
    focusRequester: FocusRequester
) {
    val mazeGrid = uiState.mazeGrid
    val playerRow = uiState.playerRow
    val playerCol = uiState.playerCol
    val endCell = uiState.endCell
    val visitedPath = uiState.visitedPath
    val solutionPath = uiState.solutionPath
    val showSolution = uiState.showSolution
    val onMove = uiState.onMove

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F0F12))
            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.key) {
                        Key.DirectionUp, Key.W -> {
                            onMove(MazeDirection.UP)
                            true
                        }

                        Key.DirectionDown, Key.S -> {
                            onMove(MazeDirection.DOWN)
                            true
                        }

                        Key.DirectionLeft, Key.A -> {
                            onMove(MazeDirection.LEFT)
                            true
                        }

                        Key.DirectionRight, Key.D -> {
                            onMove(MazeDirection.RIGHT)
                            true
                        }

                        else -> false
                    }
                } else false
            },
        contentAlignment = Alignment.Center
    ) {
        if (mazeGrid.isNotEmpty()) {
            val infiniteTransition = rememberInfiniteTransition(label = "beacon")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 0.7f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse"
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                val rows = mazeGrid.size
                val cols = mazeGrid[0].size
                val cellWidth = size.width / cols
                val cellHeight = size.height / rows
                val wallThickness = 6f
                val wallColor = Color(0xFF00E5FF) // Cyber Cyan
                val pathColor = Color(0xFF00FF66) // Neon Green
                val solutionColor = Color(0xFFFF007F) // Electric Magenta

                drawVisitedTrail(visitedPath, cellWidth, cellHeight, wallThickness, pathColor)
                drawSolutionPath(showSolution, solutionPath, cellWidth, cellHeight, wallThickness, solutionColor)
                drawMazeWalls(mazeGrid, cellWidth, cellHeight, wallThickness, wallColor)
                drawGoalBeacon(endCell, cellWidth, cellHeight, pulseScale)
                drawPlayer(playerRow, playerCol, cellWidth, cellHeight, pathColor)
            }
        }
    }
}


@PreviewDefault
@Composable
private fun MazeContentPreview() {
    AppTheme {
        val testGrid = List(8) { r ->
            List(8) { c ->
                MazeCell(r, c, hasTopWall = r == 0, hasLeftWall = c == 0, hasBottomWall = r == 7, hasRightWall = c == 7)
            }
        }
        MazeContent(
            uiState = MazeUiState.Ready(
                mazeGrid = testGrid,
                playerRow = 1,
                playerCol = 2,
                endCell = CellPos(7, 7),
                visitedPath = listOf(CellPos(0, 0), CellPos(1, 0), CellPos(1, 1), CellPos(1, 2)),
                solutionPath = emptyList(),
                showSolution = false,
                moveCount = 4,
                isGameCompleted = false,
                timeElapsed = 12500L,
                difficulty = MazeDifficulty.EASY,
                onMove = {},
                onToggleSolution = {},
                onRegenerate = {},
                onDifficultyChange = {}
            )
        )
    }
}
