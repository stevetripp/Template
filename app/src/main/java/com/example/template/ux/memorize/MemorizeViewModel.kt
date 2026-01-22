package com.example.template.ux.memorize

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.lds.mobile.ext.stateInDefault

class MemorizeViewModel : ViewModel() {

    private val textFlow = MutableStateFlow(
        "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac " +
                "placerat dolor lectus quis orci. Donec vitae orci sed dolor rutrum auctor. Sed aliquam ultrices mauris."
    )

    private val sliderPositionFlow = MutableStateFlow(0f)

    private val _rangesFlow = textFlow.map { text ->
        val ranges = mutableListOf<AnnotatedString.Range<Boolean>>()
        val regex = Regex("[a-zA-Z]+")
        var regExStartIndex = 0
        var match = regex.find(text, startIndex = regExStartIndex)

        while (match != null) {
            val matchStartIndex = text.indexOf(match.value, regExStartIndex)
            val endIndex = matchStartIndex + match.value.length - 1
            ranges.add(AnnotatedString.Range(false, matchStartIndex, endIndex))
            regExStartIndex = endIndex + 1
            match = regex.find(text, regExStartIndex)
        }

        ranges.shuffled()
    }

    private val rangesFlow = combine(sliderPositionFlow, _rangesFlow) { sliderPosition, ranges ->
        val wordsToHide = (ranges.size * sliderPosition).toInt()
        ranges.mapIndexed { index, range -> range.copy(item = index < wordsToHide) }
    }.stateInDefault(viewModelScope, emptyList())

    val uiState = MemorizeUiState(
        sliderPositionFlow = sliderPositionFlow,
        textFlow = textFlow,
        rangesFlow = rangesFlow,
        onSliderPositionChanged = { sliderPositionFlow.value = it }
    )
}