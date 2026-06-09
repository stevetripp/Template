package com.example.template.ux.maze

/**
 * Data class representing an individual cell in a maze grid.
 *
 * @property row The row index (0-indexed).
 * @property col The column index (0-indexed).
 * @property hasTopWall True if the cell has a wall on its top boundary.
 * @property hasRightWall True if the cell has a wall on its right boundary.
 * @property hasBottomWall True if the cell has a wall on its bottom boundary.
 * @property hasLeftWall True if the cell has a wall on its left boundary.
 * @property visited True if this cell has been visited by the generator algorithm.
 */
data class MazeCell(
    val row: Int,
    val col: Int,
    var hasTopWall: Boolean = true,
    var hasRightWall: Boolean = true,
    var hasBottomWall: Boolean = true,
    var hasLeftWall: Boolean = true,
    var visited: Boolean = false
)