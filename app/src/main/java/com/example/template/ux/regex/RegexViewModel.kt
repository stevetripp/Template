package com.example.template.ux.regex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.datastore.AppPreferenceDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegexViewModel @Inject constructor(
    private val appPrefs: AppPreferenceDataSource,
) : ViewModel() {

    private val textFlow = MutableStateFlow("")
    private val regexTextFlow = MutableStateFlow("")
    private val resultsTextFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            textFlow.value = appPrefs.regexTextFlow.first()
            regexTextFlow.value = appPrefs.regexExpressionFlow.first()
        }
    }

    val uiState = RegexUiState(
        textFlow = textFlow,
        regexTextFlow = regexTextFlow,
        resultsTextFlow = resultsTextFlow,
        onTextChange = ::onTextChange,
        onRegexTextChange = ::onRegexTextChange
    )

    private fun onTextChange(text: String) {
        textFlow.value = text
        updateResults()
    }

    private fun onRegexTextChange(regexText: String) {
        regexTextFlow.value = regexText
        updateResults()
    }

    private fun updateResults() = viewModelScope.launch {
        try {
            val regex = Regex(regexTextFlow.value, RegexOption.IGNORE_CASE)
            val matches = regex.findAll(textFlow.value).toList()

            val results = StringBuilder()

            matches.forEach { match ->
                results.appendLine(match.value)
            }
            resultsTextFlow.value = results.toString()

            appPrefs.setRegexText(textFlow.value)
            appPrefs.setRegexExpression(regexTextFlow.value)
        } catch (_: Exception) {
        }
    }
}