package com.example.template.ux.maze

/**
 * Sealed interface representing the UI states of the Maze screen.
 */
sealed interface MazeUiState {

    /**
     * State indicating that a new maze grid is currently being generated.
     */
    data object Loading : MazeUiState

    /**
     * State indicating that the maze is ready to be displayed and played.
     *
     * @property mazeGrid The 2D grid of cells composing the maze.
     * @property playerRow The current row position of the player.
     * @property playerCol The current column position of the player.
     * @property endCell The destination cell coordinates.
     * @property visitedPath The list of coordinate positions the player has walked.
     * @property solutionPath The shortest path from the player to the exit, computed by the solver.
     * @property showSolution True if the solver path should be rendered overlaying the maze.
     * @property moveCount The number of moves made by the player in the current session.
     * @property isGameCompleted True if the player has successfully reached the end cell.
     * @property timeElapsed The time in milliseconds since the start of the current maze session.
     * @property difficulty The active difficulty configuration.
     * @property onMove Callback triggered when the player tries to move in a given direction.
     * @property onToggleSolution Callback triggered to toggle the display of the pathfinder solver.
     * @property onRegenerate Callback triggered to discard the current game and generate a new maze.
     * @property onDifficultyChange Callback triggered to change the difficulty setting and rebuild the maze.
     */
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
        val difficulty: MazeDifficulty,
        val onMove: (MazeDirection) -> Unit,
        val onToggleSolution: () -> Unit,
        val onRegenerate: () -> Unit,
        val onDifficultyChange: (MazeDifficulty) -> Unit
    ) : MazeUiState
}