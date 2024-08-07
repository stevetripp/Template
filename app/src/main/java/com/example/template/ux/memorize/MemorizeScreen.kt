package com.example.template.ux.memorize

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.UnderlinedRedactedText
import com.example.template.ux.main.Screen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MemorizeScreen(navController: NavController, viewModel: MemorizeViewModel = hiltViewModel()) {
    MemorizeContent(viewModel.uiState, navController::popBackStack)
}

@Composable
fun MemorizeContent(uiState: MemorizeUiState, onBack: () -> Unit = {}) {
    val text by uiState.textFlow.collectAsStateWithLifecycle()
    val ranges by uiState.rangesFlow.collectAsStateWithLifecycle()
    val sliderPosition by uiState.sliderPositionFlow.collectAsStateWithLifecycle()
    var position by remember { mutableFloatStateOf(sliderPosition) }

    Scaffold(topBar = { AppTopAppBar(title = Screen.MEMORIZE.title, onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            UnderlinedRedactedText(
                text = text,
                ranges = ranges,
            )

            Slider(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = position,
                onValueChange = { position = it },
                onValueChangeFinished = { uiState.onSliderPositionChanged(position) }
            )
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    val ranges = listOf(
        AnnotatedString.Range(false, 7, 12, ""),
        AnnotatedString.Range(true, 30, 32, ""),
        AnnotatedString.Range(false, 47, 52, ""),
        AnnotatedString.Range(true, 59, 62, ""),
    )

    AppTheme { MemorizeContent(MemorizeUiState(rangesFlow = MutableStateFlow(ranges))) }
}