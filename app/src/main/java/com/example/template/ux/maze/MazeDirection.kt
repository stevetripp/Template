package com.example.template.ux.maze

/**
 * Enum representing direction inputs for moving the player inside the maze.
 */
enum class MazeDirection {
    /** Move up. */
    UP,
    /** Move down. */
    DOWN,
    /** Move left. */
    LEFT,
    /** Move right. */
    RIGHT;

    /**
     * Returns the opposite direction of this MazeDirection.
     */
    fun opposite(): MazeDirection {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}