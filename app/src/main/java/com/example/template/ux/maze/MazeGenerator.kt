package com.example.template.ux.maze

/**
 * Utility generator for creating randomized, perfect mazes.
 *
 * A "perfect" maze is defined as a maze with no closed loops and exactly
 * one unique path between any two cells in the grid. This generator uses
 * a randomized Depth-First Search (DFS) recursive backtracker algorithm.
 *
 * To prevent branches and dead-ends from extending beyond the exit point,
 * the maze paths are generated in reverse order, starting from the end
 * cell at `(rows - 1, cols - 1)` and generating back toward `(0, 0)`.
 */
object MazeGenerator {

    /**
     * Generates a randomized perfect maze grid with the specified dimensions.
     * The generator starts path carving at the end cell `(rows - 1, cols - 1)`
     * and constructs the maze in reverse towards the start cell `(0, 0)`.
     *
     * @param rows The number of rows in the maze grid.
     * @param cols The number of columns in the maze grid.
     * @return A 2D list representational grid of [MazeCell] objects.
     */
    fun generate(rows: Int, cols: Int): List<List<MazeCell>> {
        val tempGrid = List(rows) { r ->
            List(cols) { c ->
                MazeCell(r, c)
            }
        }

        val stack = ArrayDeque<MazeCell>()
        val start = tempGrid[rows - 1][cols - 1]
        start.visited = true
        stack.addLast(start)

        while (stack.isNotEmpty()) {
            val current = stack.last()
            val neighbors = getUnvisitedNeighbors(current, tempGrid, rows, cols)

            if (neighbors.isNotEmpty()) {
                // Select a random unvisited neighbor, carve a path to it, and push it to the stack
                val next = neighbors.random()
                next.visited = true
                removeWalls(current, next)
                stack.addLast(next)
            } else {
                // Backtrack if no unvisited neighbors are available
                stack.removeLast()
            }
        }

        // Carve entry at start cell (0, 0) along the left border
        tempGrid[0][0].hasLeftWall = false

        // Carve exit at end cell (rows - 1, cols - 1) along the right border
        tempGrid[rows - 1][cols - 1].hasRightWall = false

        return tempGrid
    }

    /**
     * Retrieves all adjacent cells that have not yet been visited by the generator.
     *
     * @param cell The current cell to search neighbors from.
     * @param grid The active grid list.
     * @param rows The total rows count.
     * @param cols The total columns count.
     * @return A list of unvisited neighbor [MazeCell]s.
     */
    private fun getUnvisitedNeighbors(cell: MazeCell, grid: List<List<MazeCell>>, rows: Int, cols: Int): List<MazeCell> {
        val neighbors = mutableListOf<MazeCell>()
        val r = cell.row
        val c = cell.col
        if (r > 0 && !grid[r - 1][c].visited) neighbors.add(grid[r - 1][c])
        if (r < rows - 1 && !grid[r + 1][c].visited) neighbors.add(grid[r + 1][c])
        if (c > 0 && !grid[r][c - 1].visited) neighbors.add(grid[r][c - 1])
        if (c < cols - 1 && !grid[r][c + 1].visited) neighbors.add(grid[r][c + 1])
        return neighbors
    }

    /**
     * Carves a passage between two adjacent cells by removing their mutual dividing walls.
     *
     * @param c1 The first cell.
     * @param c2 The second cell, adjacent to the first cell.
     */
    private fun removeWalls(c1: MazeCell, c2: MazeCell) {
        val rDiff = c1.row - c2.row
        val cDiff = c1.col - c2.col
        if (rDiff == 1) { // c1 is below c2
            c1.hasTopWall = false
            c2.hasBottomWall = false
        } else if (rDiff == -1) { // c1 is above c2
            c1.hasBottomWall = false
            c2.hasTopWall = false
        } else if (cDiff == 1) { // c1 is right of c2
            c1.hasLeftWall = false
            c2.hasRightWall = false
        } else if (cDiff == -1) { // c1 is left of c2
            c1.hasRightWall = false
            c2.hasLeftWall = false
        }
    }
}
