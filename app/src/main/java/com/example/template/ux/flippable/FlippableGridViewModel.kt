package com.example.template.ux.flippable

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FlippableGridViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FlippableGridUiState())
    val uiState: StateFlow<FlippableGridUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                onXModeChanged = ::onXModeChanged,
                triggerFlip = ::triggerFlip
            )
        }
    }

    private fun onXModeChanged(isXMode: Boolean) {
        _uiState.update { it.copy(isXMode = isXMode) }
    }

    private fun triggerFlip(index: Int) {
        _uiState.update { state ->
            val updatedSymbols = state.cellSymbols.toMutableList()
            val updatedIsFlipped = state.cellIsFlipped.toMutableList()

            val isCurrentlyFlipped = updatedIsFlipped[index]
            if (!isCurrentlyFlipped) {
                updatedSymbols[index] = if (state.isXMode) CellSymbol.X else CellSymbol.SQUARE
            }
            updatedIsFlipped[index] = !isCurrentlyFlipped

            state.copy(
                cellSymbols = updatedSymbols,
                cellIsFlipped = updatedIsFlipped
            )
        }
    }
}
