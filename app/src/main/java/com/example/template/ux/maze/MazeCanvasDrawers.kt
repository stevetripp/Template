package com.example.template.ux.maze

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Draws the path history the player has visited.
 *
 * @param visitedPath List of grid coordinates representing the player's visited path.
 * @param cellWidth The width of a single maze cell.
 * @param cellHeight The height of a single maze cell.
 * @param wallThickness The stroke width of the path and walls.
 * @param pathColor The primary color of the player's trail.
 */
fun DrawScope.drawVisitedTrail(
    visitedPath: List<CellPos>,
    cellWidth: Float,
    cellHeight: Float,
    wallThickness: Float,
    pathColor: Color
) {
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
}

/**
 * Draws the BFS calculated shortest path solution.
 *
 * @param showSolution Flag indicating whether the solver path is visible.
 * @param solutionPath List of coordinates forming the optimal path.
 * @param cellWidth The width of a single maze cell.
 * @param cellHeight The height of a single maze cell.
 * @param wallThickness The stroke width of the path.
 * @param solutionColor The color representing the solver's path.
 */
fun DrawScope.drawSolutionPath(
    showSolution: Boolean,
    solutionPath: List<CellPos>,
    cellWidth: Float,
    cellHeight: Float,
    wallThickness: Float,
    solutionColor: Color
) {
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
}

/**
 * Draws all walls of the maze grid.
 *
 * @param mazeGrid The 2D grid of maze cells representing the walls layout.
 * @param cellWidth The width of a single maze cell.
 * @param cellHeight The height of a single maze cell.
 * @param wallThickness The stroke width of the walls.
 * @param wallColor The color representing the maze walls.
 */
fun DrawScope.drawMazeWalls(
    mazeGrid: List<List<MazeCell>>,
    cellWidth: Float,
    cellHeight: Float,
    wallThickness: Float,
    wallColor: Color
) {
    val rows = mazeGrid.size
    val cols = mazeGrid[0].size
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
}

/**
 * Draws the exit goal beacon with a pulsing animation.
 *
 * @param endCell The target exit cell coordinate.
 * @param cellWidth The width of a single maze cell.
 * @param cellHeight The height of a single maze cell.
 * @param pulseScale Current scale factor for the pulsing outer glow.
 */
fun DrawScope.drawGoalBeacon(
    endCell: CellPos,
    cellWidth: Float,
    cellHeight: Float,
    pulseScale: Float
) {
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
}

/**
 * Draws the player dot and its neon glow.
 *
 * @param playerRow Current row index of the player.
 * @param playerCol Current column index of the player.
 * @param cellWidth The width of a single maze cell.
 * @param cellHeight The height of a single maze cell.
 * @param pathColor The color representing the player and its glow.
 */
fun DrawScope.drawPlayer(
    playerRow: Int,
    playerCol: Int,
    cellWidth: Float,
    cellHeight: Float,
    pathColor: Color
) {
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
