package com.example.template.ux.maze

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

/**
 * ViewModel for managing the state and game loop of the Maze game screen.
 *
 * Implements [ViewModelNavigation3] for routing. It manages the backing properties
 * for [MazeUiState], generates randomized perfect mazes using [MazeGenerator],
 * handles boundary checked player movements, computes paths using a BFS solver,
 * and maintains a coroutine game timer.
 */
class MazeViewModel : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    private val _uiState = MutableStateFlow<MazeUiState>(MazeUiState.Loading)

    /**
     * Observable flow of the active [MazeUiState].
     */
    val uiState = _uiState.asStateFlow()

    // Backing properties for the Ready state
    private var mazeGrid: List<List<MazeCell>> = emptyList()
    private var playerRow: Int = 0
    private var playerCol: Int = 0
    private var endCell: CellPos = CellPos(0, 0)
    private var visitedPath: List<CellPos> = emptyList()
    private var solutionPath: List<CellPos> = emptyList()
    private var showSolution: Boolean = false
    private var moveCount: Int = 0
    private var isGameCompleted: Boolean = false
    private var timeElapsed: Long = 0L
    private var difficulty: MazeDifficulty = MazeDifficulty.EASY
    private var isLoading: Boolean = true

    private var timerJob: Job? = null

    init {
        generateNewMaze()
    }

    /**
     * Unifies backing properties and emits a new [MazeUiState.Ready] or [MazeUiState.Loading] state.
     */
    private fun updateState() {
        if (isLoading) {
            _uiState.value = MazeUiState.Loading
        } else {
            _uiState.value = MazeUiState.Ready(
                mazeGrid = mazeGrid,
                playerRow = playerRow,
                playerCol = playerCol,
                endCell = endCell,
                visitedPath = visitedPath,
                solutionPath = solutionPath,
                showSolution = showSolution,
                moveCount = moveCount,
                isGameCompleted = isGameCompleted,
                timeElapsed = timeElapsed,
                difficulty = difficulty,
                onMove = ::movePlayer,
                onToggleSolution = ::toggleSolution,
                onRegenerate = ::generateNewMaze,
                onDifficultyChange = ::setDifficulty
            )
        }
    }

    /**
     * Sets a custom maze grid and resets state for predictable unit testing.
     */
    @VisibleForTesting
    internal fun setGridForTesting(
        grid: List<List<MazeCell>>,
        startRow: Int = 0,
        startCol: Int = 0,
        endRow: Int = grid.size - 1,
        endCol: Int = grid[0].size - 1
    ) {
        stopTimer()
        mazeGrid = grid
        playerRow = startRow
        playerCol = startCol
        endCell = CellPos(endRow, endCol)
        visitedPath = listOf(CellPos(startRow, startCol))
        solutionPath = emptyList()
        showSolution = false
        moveCount = 0
        isGameCompleted = false
        timeElapsed = 0L
        isLoading = false
        updateState()
    }

    /**
     * Sets a new difficulty setting and regenerates the maze.
     *
     * @param newDifficulty The difficulty to select.
     */
    fun setDifficulty(newDifficulty: MazeDifficulty) {
        difficulty = newDifficulty
        generateNewMaze()
    }

    /**
     * Generates a completely new randomized maze, resets player positions,
     * resets stats/solution trail, and starts/restarts the stopwatch timer.
     */
    fun generateNewMaze() {
        isLoading = true
        updateState()

        val rows = difficulty.rows
        val cols = difficulty.cols

        mazeGrid = MazeGenerator.generate(rows, cols)
        playerRow = 0
        playerCol = 0
        endCell = CellPos(rows - 1, cols - 1)
        visitedPath = listOf(CellPos(0, 0))
        solutionPath = emptyList()
        showSolution = false
        moveCount = 0
        isGameCompleted = false
        timeElapsed = 0L
        isLoading = false

        updateState()
        startTimer()
    }

    /**
     * Moves the player in the specified direction if the path is not blocked by a wall.
     * Continues moving the player automatically along straight paths and forced corridors
     * until a decision point (intersection or dead-end) is reached.
     * Checks boundaries, increments the move counter, records the visited path,
     * and stops the timer on game completion.
     *
     * @param direction The [MazeDirection] to move.
     */
    fun movePlayer(direction: MazeDirection) {
        if (isGameCompleted || isLoading) return

        val initialStep = getNextCell(playerRow, playerCol, direction) ?: return
        moveCount += 1

        val pathList = calculateAutoWalkPath(initialStep, direction)
        val finalCell = pathList.last()
        playerRow = finalCell.row
        playerCol = finalCell.col

        val currentPath = visitedPath.toMutableList()
        currentPath.addAll(pathList)
        visitedPath = currentPath

        if (finalCell == endCell) {
            isGameCompleted = true
            stopTimer()
        }

        if (showSolution) {
            calculateSolution()
        } else {
            updateState()
        }
    }

    /**
     * Calculates the path traversed by auto-walking starting from [start] in [direction].
     * Walks automatically along straight paths and forced corners until reaching an intersection,
     * a dead end, or the exit cell.
     */
    private fun calculateAutoWalkPath(start: CellPos, direction: MazeDirection): List<CellPos> {
        val path = mutableListOf<CellPos>()
        val visited = mutableSetOf(CellPos(playerRow, playerCol), start)
        path.add(start)

        var current = start
        var lastDir = direction
        var keepWalking = true

        while (current != endCell && keepWalking) {
            val nextDir = getNextAutoWalkDirection(current, lastDir)
            if (nextDir != null) {
                val nextPos = getNextCell(current.row, current.col, nextDir)
                if (nextPos != null && nextPos !in visited) {
                    visited.add(nextPos)
                    path.add(nextPos)
                    current = nextPos
                    lastDir = nextDir
                } else {
                    keepWalking = false
                }
            } else {
                keepWalking = false
            }
        }
        return path
    }

    /**
     * Determines the next direction to move during an auto-walk, if forced.
     * Returns the single forward direction if there is exactly one way forward, or null otherwise.
     */
    private fun getNextAutoWalkDirection(current: CellPos, lastDir: MazeDirection): MazeDirection? {
        val availableDirs = getAvailableDirections(current.row, current.col)
        val oppositeDir = lastDir.opposite()
        val forwardDirs = availableDirs.filter { it != oppositeDir }
        return if (forwardDirs.size == 1) forwardDirs[0] else null
    }

    /**
     * Calculates the coordinate of the adjacent cell in the given direction if open.
     */
    private fun getNextCell(row: Int, col: Int, direction: MazeDirection): CellPos? {
        val grid = mazeGrid
        if (grid.isEmpty()) return null
        val cell = grid[row][col]
        return when (direction) {
            MazeDirection.UP -> if (!cell.hasTopWall && row > 0) CellPos(row - 1, col) else null
            MazeDirection.DOWN -> if (!cell.hasBottomWall && row < grid.size - 1) CellPos(row + 1, col) else null
            MazeDirection.LEFT -> if (!cell.hasLeftWall && col > 0) CellPos(row, col - 1) else null
            MazeDirection.RIGHT -> if (!cell.hasRightWall && col < grid[0].size - 1) CellPos(row, col + 1) else null
        }
    }

    /**
     * Helper to find all valid, open directions from a cell that do not cross walls or boundaries.
     */
    private fun getAvailableDirections(row: Int, col: Int): List<MazeDirection> {
        val grid = mazeGrid
        if (grid.isEmpty()) return emptyList()
        val cell = grid[row][col]
        val list = mutableListOf<MazeDirection>()
        if (!cell.hasTopWall && row > 0) list.add(MazeDirection.UP)
        if (!cell.hasBottomWall && row < grid.size - 1) list.add(MazeDirection.DOWN)
        if (!cell.hasLeftWall && col > 0) list.add(MazeDirection.LEFT)
        if (!cell.hasRightWall && col < grid[0].size - 1) list.add(MazeDirection.RIGHT)
        return list
    }

    /**
     * Toggles the display overlay of the shortest-path solver.
     */
    fun toggleSolution() {
        if (isLoading) return
        if (showSolution) {
            showSolution = false
            updateState()
        } else {
            calculateSolution()
        }
    }

    /**
     * Internal solver function using Breadth-First Search (BFS) to find
     * and record the shortest path from the player's position to the endCell.
     */
    private fun calculateSolution() {
        val grid = mazeGrid
        if (grid.isEmpty()) return

        val rows = grid.size
        val cols = grid[0].size
        val start = CellPos(playerRow, playerCol)
        val end = endCell

        val queue = ArrayDeque<List<CellPos>>()
        queue.addLast(listOf(start))

        val visited = mutableSetOf<CellPos>()
        visited.add(start)

        var solvedPath = emptyList<CellPos>()

        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            val current = path.last()

            if (current == end) {
                solvedPath = path
                break
            }

            val cell = grid[current.row][current.col]
            val r = current.row
            val c = current.col

            // Up
            if (!cell.hasTopWall && r > 0) {
                val next = CellPos(r - 1, c)
                if (next !in visited) {
                    visited.add(next)
                    queue.addLast(path + next)
                }
            }
            // Down
            if (!cell.hasBottomWall && r < rows - 1) {
                val next = CellPos(r + 1, c)
                if (next !in visited) {
                    visited.add(next)
                    queue.addLast(path + next)
                }
            }
            // Left
            if (!cell.hasLeftWall && c > 0) {
                val next = CellPos(r, c - 1)
                if (next !in visited) {
                    visited.add(next)
                    queue.addLast(path + next)
                }
            }
            // Right
            if (!cell.hasRightWall && c < cols - 1) {
                val next = CellPos(r, c + 1)
                if (next !in visited) {
                    visited.add(next)
                    queue.addLast(path + next)
                }
            }
        }

        solutionPath = solvedPath
        showSolution = true
        updateState()
    }

    /**
     * Starts the coroutine-based game timer.
     */
    private fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            while (isActive) {
                delay(100)
                timeElapsed = System.currentTimeMillis() - startTime
                updateState()
            }
        }
    }

    /**
     * Cancels the active timer coroutine job.
     */
    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}
