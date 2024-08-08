package com.example.template.ux.regex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.datastore.AppPreferenceDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.lds.mobile.ext.stateInDefault
import javax.inject.Inject

@HiltViewModel
class RegexViewModel @Inject constructor(
    private val appPrefs: AppPreferenceDataSource,
) : ViewModel() {

    private val textFlow = MutableStateFlow("")
    private val regexTextFlow = MutableStateFlow("")

    private val multilineModeEnabledFlow = appPrefs.multilineModeEnabledFlow.stateInDefault(viewModelScope, false)

    private val resultsTextFlow = combine(textFlow, regexTextFlow, multilineModeEnabledFlow) { text, regexText, multilineEnabled ->
        try {
            val options = mutableSetOf(RegexOption.IGNORE_CASE).apply {
                if (multilineEnabled) add(RegexOption.MULTILINE)
            }
            val regex = Regex(regexText, options)
            val matches = regex.findAll(text).toList()

            val results = StringBuilder()

            matches.forEach { match ->
                results.appendLine(match.value)
            }

            appPrefs.setRegexText(text)
            appPrefs.setRegexExpression(regexText)

            results.toString()
        } catch (expected: Exception) {
            expected.toString()
        }
    }.stateInDefault(viewModelScope, "")

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
        multilineModeEnabledFlow = multilineModeEnabledFlow,
        onTextChange = { textFlow.value = it },
        onRegexTextChange = { regexTextFlow.value = it },
        onToggleMultilineMode = { appPrefs.setMultilineModeEnabledAsync(it) }
    )
}