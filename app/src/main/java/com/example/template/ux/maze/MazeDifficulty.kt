package com.example.template.ux.maze

/**
 * Enum representing the available difficulty configurations for the Maze game.
 *
 * Each difficulty determines the size of the generated maze grid.
 *
 * @property title The user-friendly title of the difficulty level.
 * @property rows The number of rows for the generated maze.
 * @property cols The number of columns for the generated maze.
 */
enum class MazeDifficulty(val title: String, val rows: Int, val cols: Int) {
    /**
     * Easy setting with a 10x10 grid size.
     */
    EASY("Easy (10x10)", 10, 10),

    /**
     * Medium setting with a 15x15 grid size.
     */
    MEDIUM("Medium (15x15)", 15, 15),

    /**
     * Hard setting with a 20x20 grid size.
     */
    HARD("Hard (20x20)", 20, 20)
}