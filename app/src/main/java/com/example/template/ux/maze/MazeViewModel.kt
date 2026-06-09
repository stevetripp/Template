package com.example.template.ux.maze

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

data class CellPos(val row: Int, val col: Int)

data class MazeCell(
    val row: Int,
    val col: Int,
    var hasTopWall: Boolean = true,
    var hasRightWall: Boolean = true,
    var hasBottomWall: Boolean = true,
    var hasLeftWall: Boolean = true,
    var visited: Boolean = false
)

enum class Difficulty(val title: String, val rows: Int, val cols: Int) {
    EASY("Easy (10x10)", 10, 10),
    MEDIUM("Medium (15x15)", 15, 15),
    HARD("Hard (20x20)", 20, 20)
}

enum class MazeDirection { UP, DOWN, LEFT, RIGHT }

class MazeViewModel : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    private val _uiState = MutableStateFlow<MazeUiState>(MazeUiState.Loading)
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
    private var difficulty: Difficulty = Difficulty.EASY
    private var isLoading: Boolean = true

    private var timerJob: Job? = null

    init {
        generateNewMaze()
    }

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

    fun setDifficulty(newDifficulty: Difficulty) {
        difficulty = newDifficulty
        generateNewMaze()
    }

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

    fun movePlayer(direction: MazeDirection) {
        if (isGameCompleted || isLoading) return
        val grid = mazeGrid
        if (grid.isEmpty()) return

        val r = playerRow
        val c = playerCol
        val cell = grid[r][c]

        var newRow = r
        var newCol = c
        var moved = false

        when (direction) {
            MazeDirection.UP -> {
                if (!cell.hasTopWall && r > 0) {
                    newRow = r - 1
                    moved = true
                }
            }
            MazeDirection.DOWN -> {
                if (!cell.hasBottomWall && r < grid.size - 1) {
                    newRow = r + 1
                    moved = true
                }
            }
            MazeDirection.LEFT -> {
                if (!cell.hasLeftWall && c > 0) {
                    newCol = c - 1
                    moved = true
                }
            }
            MazeDirection.RIGHT -> {
                if (!cell.hasRightWall && c < grid[0].size - 1) {
                    newCol = c + 1
                    moved = true
                }
            }
        }

        if (moved) {
            playerRow = newRow
            playerCol = newCol
            moveCount += 1

            val newPos = CellPos(newRow, newCol)
            val currentPath = visitedPath.toMutableList()
            currentPath.add(newPos)
            visitedPath = currentPath

            if (newPos == endCell) {
                isGameCompleted = true
                stopTimer()
            }

            if (showSolution) {
                calculateSolution()
            } else {
                updateState()
            }
        }
    }

    fun toggleSolution() {
        if (isLoading) return
        if (showSolution) {
            showSolution = false
            updateState()
        } else {
            calculateSolution()
        }
    }

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

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}
