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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import java.util.Locale
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun MazeScreen(navigator: Navigation3Navigator, viewModel: MazeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MazeUiState.Loading -> MazeLoadingScreen(onBack = navigator::pop)
        is MazeUiState.Ready -> MazeContent(uiState = state, onBack = navigator::pop)
    }
}

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
                            Difficulty.entries.forEach { diff ->
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
        val mazeGrid = uiState.mazeGrid
        val playerRow = uiState.playerRow
        val playerCol = uiState.playerCol
        val endCell = uiState.endCell
        val visitedPath = uiState.visitedPath
        val solutionPath = uiState.solutionPath
        val showSolution = uiState.showSolution
        val moveCount = uiState.moveCount
        val isGameCompleted = uiState.isGameCompleted
        val timeElapsed = uiState.timeElapsed
        val onMove = uiState.onMove
        val onRegenerate = uiState.onRegenerate

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
            // Stats Panel
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
                        Text(
                            text = String.format(Locale.getDefault(), "%.1fs", seconds),
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

            // Maze Canvas Container
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

                        // 1. Draw Visited Trail
                        if (visitedPath.size > 1) {
                            val trailPath = Path().apply {
                                val startX = visitedPath[0].col * cellWidth + cellWidth / 2
                                val startY = visitedPath[0].row * cellHeight + cellHeight / 2
                                moveTo(startX, startY)
                                for (i in 1 until visitedPath.size) {
                                    val x = visitedPath[i].col * cellWidth + cellWidth / 2
                                    val y = visitedPath[i].row * cellHeight + cellHeight / 2
                                    lineTo(x, y)
                                }
                            }
                            drawPath(
                                path = trailPath,
                                color = pathColor.copy(alpha = 0.35f),
                                style = Stroke(width = wallThickness * 1.5f, cap = StrokeCap.Round)
                            )
                        }

                        // 2. Draw Solver Solution (BFS)
                        if (showSolution && solutionPath.size > 1) {
                            val solPath = Path().apply {
                                val startX = solutionPath[0].col * cellWidth + cellWidth / 2
                                val startY = solutionPath[0].row * cellHeight + cellHeight / 2
                                moveTo(startX, startY)
                                for (i in 1 until solutionPath.size) {
                                    val x = solutionPath[i].col * cellWidth + cellWidth / 2
                                    val y = solutionPath[i].row * cellHeight + cellHeight / 2
                                    lineTo(x, y)
                                }
                            }
                            drawPath(
                                path = solPath,
                                color = solutionColor,
                                style = Stroke(width = wallThickness * 1.2f, cap = StrokeCap.Round)
                            )
                        }

                        // 3. Draw Maze Walls
                        for (r in 0 until rows) {
                            for (c in 0 until cols) {
                                val cell = mazeGrid[r][c]
                                val left = c * cellWidth
                                val top = r * cellHeight
                                val right = (c + 1) * cellWidth
                                val bottom = (r + 1) * cellHeight

                                if (cell.hasTopWall) {
                                    drawLine(
                                        color = wallColor,
                                        start = Offset(left, top),
                                        end = Offset(right, top),
                                        strokeWidth = wallThickness,
                                        cap = StrokeCap.Round
                                    )
                                }
                                if (cell.hasLeftWall) {
                                    drawLine(
                                        color = wallColor,
                                        start = Offset(left, top),
                                        end = Offset(left, bottom),
                                        strokeWidth = wallThickness,
                                        cap = StrokeCap.Round
                                    )
                                }
                                if (cell.hasBottomWall) {
                                    drawLine(
                                        color = wallColor,
                                        start = Offset(left, bottom),
                                        end = Offset(right, bottom),
                                        strokeWidth = wallThickness,
                                        cap = StrokeCap.Round
                                    )
                                }
                                if (cell.hasRightWall) {
                                    drawLine(
                                        color = wallColor,
                                        start = Offset(right, top),
                                        end = Offset(right, bottom),
                                        strokeWidth = wallThickness,
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                        }

                        // 4. Draw Goal (Beacon)
                        val goalX = endCell.col * cellWidth + cellWidth / 2
                        val goalY = endCell.row * cellHeight + cellHeight / 2
                        val baseRadius = cellWidth.coerceAtMost(cellHeight) / 3.5f

                        // Outer pulsing glow
                        drawCircle(
                            color = Color(0xFFFFB300).copy(alpha = 0.3f),
                            radius = baseRadius * pulseScale * 1.6f,
                            center = Offset(goalX, goalY)
                        )
                        // Inner beacon
                        drawCircle(
                            color = Color(0xFFFFB300),
                            radius = baseRadius,
                            center = Offset(goalX, goalY)
                        )

                        // 5. Draw Player
                        val playerX = playerCol * cellWidth + cellWidth / 2
                        val playerY = playerRow * cellHeight + cellHeight / 2
                        val pRadius = cellWidth.coerceAtMost(cellHeight) / 3.8f

                        // Outer player glow
                        drawCircle(
                            color = pathColor.copy(alpha = 0.4f),
                            radius = pRadius * 1.5f,
                            center = Offset(playerX, playerY)
                        )
                        // Player core
                        drawCircle(
                            color = pathColor,
                            radius = pRadius,
                            center = Offset(playerX, playerY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = pRadius * 0.4f,
                            center = Offset(playerX, playerY)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // D-Pad Control Panel
            Surface(
                modifier = Modifier
                    .padding(bottom = 32.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = { onMove(MazeDirection.UP) },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Move Up", modifier = Modifier.size(36.dp))
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onMove(MazeDirection.LEFT) },
                            modifier = Modifier.size(56.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Move Left", modifier = Modifier.size(36.dp))
                        }
                        Spacer(modifier = Modifier.size(56.dp)) // Center spacer representing where D-pad center is
                        IconButton(
                            onClick = { onMove(MazeDirection.RIGHT) },
                            modifier = Modifier.size(56.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Move Right", modifier = Modifier.size(36.dp))
                        }
                    }
                    IconButton(
                        onClick = { onMove(MazeDirection.DOWN) },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Move Down", modifier = Modifier.size(36.dp))
                    }
                }
            }

            // Victory Dialog
            if (isGameCompleted) {
                AlertDialog(
                    onDismissRequest = { /* Prevent dismiss by clicking outside */ },
                    title = {
                        Text(
                            text = "🏆 Maze Solved!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00FF66),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Congratulations, you escaped the maze!",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Time", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    val sec = timeElapsed / 1000.0
                                    Text(text = String.format(Locale.getDefault(), "%.2f s", sec), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Moves", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(text = moveCount.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = onRegenerate,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66), contentColor = Color.Black)
                        ) {
                            Text("Play Again", fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onBack) {
                            Text("Back to Menu")
                        }
                    }
                )
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
                difficulty = Difficulty.EASY,
                onMove = {},
                onToggleSolution = {},
                onRegenerate = {},
                onDifficultyChange = {}
            )
        )
    }
}
