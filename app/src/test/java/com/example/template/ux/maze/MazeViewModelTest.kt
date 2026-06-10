package com.example.template.ux.maze

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MazeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MazeViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MazeViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testAutoWalkStraightCorridor() {
        // 1x4 Grid: (0,0) <-> (0,1) <-> (0,2) <-> (0,3)
        // Walls carved between adjacent cells
        val cell00 = MazeCell(0, 0, hasRightWall = false)
        val cell01 = MazeCell(0, 1, hasLeftWall = false, hasRightWall = false)
        val cell02 = MazeCell(0, 2, hasLeftWall = false, hasRightWall = false)
        val cell03 = MazeCell(0, 3, hasLeftWall = false)
        val grid = listOf(listOf(cell00, cell01, cell02, cell03))

        viewModel.setGridForTesting(grid, startRow = 0, startCol = 0, endRow = 0, endCol = 3)

        // Move player right
        viewModel.movePlayer(MazeDirection.RIGHT)

        val state = viewModel.uiState.value as MazeUiState.Ready
        assertEquals(0, state.playerRow)
        assertEquals(3, state.playerCol)
        assertEquals(1, state.moveCount)
        assertTrue(state.isGameCompleted)

        // Traversed path should contain all steps
        assertEquals(
            listOf(CellPos(0, 0), CellPos(0, 1), CellPos(0, 2), CellPos(0, 3)),
            state.visitedPath
        )
    }

    @Test
    fun testAutoWalkAroundCorner() {
        // 2x2 Grid:
        // (0,0) <-> (0,1)
        //             |
        //           (1,1)
        val cell00 = MazeCell(0, 0, hasRightWall = false)
        val cell01 = MazeCell(0, 1, hasLeftWall = false, hasBottomWall = false)
        val cell10 = MazeCell(1, 0)
        val cell11 = MazeCell(1, 1, hasTopWall = false)
        val grid = listOf(
            listOf(cell00, cell01),
            listOf(cell10, cell11)
        )

        viewModel.setGridForTesting(grid, startRow = 0, startCol = 0, endRow = 1, endCol = 1)

        // Move player right
        viewModel.movePlayer(MazeDirection.RIGHT)

        val state = viewModel.uiState.value as MazeUiState.Ready
        assertEquals(1, state.playerRow)
        assertEquals(1, state.playerCol)
        assertEquals(1, state.moveCount)
        assertTrue(state.isGameCompleted)

        assertEquals(
            listOf(CellPos(0, 0), CellPos(0, 1), CellPos(1, 1)),
            state.visitedPath
        )
    }

    @Test
    fun testAutoWalkStopsAtIntersection() {
        // 2x3 Grid:
        // (0,0) <-> (0,1) <-> (0,2)
        //             |
        //           (1,1)
        val cell00 = MazeCell(0, 0, hasRightWall = false)
        val cell01 = MazeCell(0, 1, hasLeftWall = false, hasRightWall = false, hasBottomWall = false)
        val cell02 = MazeCell(0, 2, hasLeftWall = false)
        val cell10 = MazeCell(1, 0)
        val cell11 = MazeCell(1, 1, hasTopWall = false)
        val cell12 = MazeCell(1, 2)
        val grid = listOf(
            listOf(cell00, cell01, cell02),
            listOf(cell10, cell11, cell12)
        )

        viewModel.setGridForTesting(grid, startRow = 0, startCol = 0, endRow = 1, endCol = 1)

        // Move player right
        viewModel.movePlayer(MazeDirection.RIGHT)

        val state = viewModel.uiState.value as MazeUiState.Ready
        // Should stop at (0,1) because it has open ways to (0,2) and (1,1)
        assertEquals(0, state.playerRow)
        assertEquals(1, state.playerCol)
        assertEquals(1, state.moveCount)
        assertTrue(!state.isGameCompleted)

        assertEquals(
            listOf(CellPos(0, 0), CellPos(0, 1)),
            state.visitedPath
        )
    }
}
