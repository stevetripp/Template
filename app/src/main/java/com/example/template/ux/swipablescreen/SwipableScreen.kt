package com.example.template.ux.swipablescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlin.math.roundToInt

@Composable
fun SwipableScreen(navController: NavController) {
    SwipableContent(navController::popBackStack)
}

@Composable
fun SwipableContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.SWIPABLE.title, onBack = onBack) }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(testData) { data ->
                val swipeAnchors = mutableMapOf(0f to 0, -250F to 1)
                val swipeableState: SwipeableState<Int> = rememberSwipeableState(0)

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .swipeable(
                            state = swipeableState,
                            anchors = swipeAnchors,
                            thresholds = { _, _ -> FractionalThreshold(0.3f) },
                            orientation = Orientation.Horizontal
                        )
                ) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .matchParentSize()
                            .background(MaterialTheme.colors.primary)
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = maxHeight / 2 - (Icons.Default.Delete.defaultWidth / 2)),
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    ListItem(
                        modifier = Modifier
                            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                            .background(MaterialTheme.colors.background),
                        text = { Text(data.text) },
                        secondaryText = { Text(data.secondaryText) },
                        overlineText = { Text(data.overlineText) },
                    )
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun SwipableContentPreview() {
    AppTheme { SwipableContent() }
}

private val testData: List<TestData>
    get() {
        val data = mutableListOf<TestData>()
        for (i in 1..10) {
            data.add(TestData("Test$i", "Secondary Text$i", "Overline Text$i"))
        }
        return data
    }

private data class TestData(val text: String, val secondaryText: String, val overlineText: String)