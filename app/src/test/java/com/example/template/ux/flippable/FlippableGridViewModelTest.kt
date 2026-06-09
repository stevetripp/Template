package com.example.template.ux.flippable

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FlippableGridViewModelTest {

    @Test
    fun testInitialState() {
        val viewModel = FlippableGridViewModel()
        val state = viewModel.uiState.value

        assertFalse(state.isXMode)
        assertEquals(100, state.cellSymbols.size)
        assertEquals(100, state.cellIsFlipped.size)
        assertTrue(state.cellSymbols.all { it == CellSymbol.NONE })
        assertTrue(state.cellIsFlipped.all { !it })
    }

    @Test
    fun testOnXModeChanged() {
        val viewModel = FlippableGridViewModel()
        
        viewModel.uiState.value.onXModeChanged(true)
        assertTrue(viewModel.uiState.value.isXMode)

        viewModel.uiState.value.onXModeChanged(false)
        assertFalse(viewModel.uiState.value.isXMode)
    }

    @Test
    fun testTriggerFlip() {
        val viewModel = FlippableGridViewModel()

        // Flip cell 5 while in solid square mode (isXMode = false)
        viewModel.uiState.value.triggerFlip(5)
        
        val stateAfterFirstFlip = viewModel.uiState.value
        assertTrue(stateAfterFirstFlip.cellIsFlipped[5])
        assertEquals(CellSymbol.SQUARE, stateAfterFirstFlip.cellSymbols[5])

        // Toggle to X mode
        stateAfterFirstFlip.onXModeChanged(true)
        val stateAfterXToggle = viewModel.uiState.value
        assertTrue(stateAfterXToggle.isXMode)
        
        // Flipped cell 5 should RETAIN its symbol (SQUARE) even after switch toggling
        assertEquals(CellSymbol.SQUARE, stateAfterXToggle.cellSymbols[5])

        // Flip cell 12 while in X mode (isXMode = true)
        stateAfterXToggle.triggerFlip(12)
        
        val stateAfterSecondFlip = viewModel.uiState.value
        assertTrue(stateAfterSecondFlip.cellIsFlipped[12])
        assertEquals(CellSymbol.X, stateAfterSecondFlip.cellSymbols[12])
    }
}
