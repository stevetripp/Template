package com.example.template.ux.memorize

import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MemorizeUiState(
    val textFlow: StateFlow<String> = MutableStateFlow("Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc."),
    val rangesFlow: StateFlow<List<AnnotatedString.Range<Boolean>>> = MutableStateFlow(emptyList()),
    val sliderPositionFlow: StateFlow<Float> = MutableStateFlow(.5f),
    val onSliderPositionChanged: (Float) -> Unit = {},
)