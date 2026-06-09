package com.example.template.ux.flippable

data class FlippableGridUiState(
    val isXMode: Boolean = false,
    val cellSymbols: List<CellSymbol> = List(100) { CellSymbol.NONE },
    val cellIsFlipped: List<Boolean> = List(100) { false },
    val onXModeChanged: (Boolean) -> Unit = {},
    val triggerFlip: (Int) -> Unit = {}
)