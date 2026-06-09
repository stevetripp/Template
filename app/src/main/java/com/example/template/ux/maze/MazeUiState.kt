package com.example.template.ux.maze

sealed interface MazeUiState {
    data object Loading : MazeUiState

    data class Ready(
        val mazeGrid: List<List<MazeCell>>,
        val playerRow: Int,
        val playerCol: Int,
        val endCell: CellPos,
        val visitedPath: List<CellPos>,
        val solutionPath: List<CellPos>,
        val showSolution: Boolean,
        val moveCount: Int,
        val isGameCompleted: Boolean,
        val timeElapsed: Long,
        val difficulty: Difficulty,
        val onMove: (MazeDirection) -> Unit,
        val onToggleSolution: () -> Unit,
        val onRegenerate: () -> Unit,
        val onDifficultyChange: (Difficulty) -> Unit
    ) : MazeUiState
}