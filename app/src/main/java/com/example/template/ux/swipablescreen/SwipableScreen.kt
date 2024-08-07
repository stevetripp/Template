package com.example.template.ux.swipablescreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SwipableScreen(navController: NavController) {
    SwipableContent(navController::popBackStack)
}

@Composable
fun SwipableContent(onBack: () -> Unit = {}) {
    var items by remember { mutableStateOf(testItems) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { AppTopAppBar(title = Screen.SWIPABLE.title, onBack = onBack) }) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(items, key = { it.id }) { item ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { dismissValue ->
                        Log.i("SMT", "DismissValue: $dismissValue")
                        when (dismissValue) {
                            SwipeToDismissBoxValue.StartToEnd -> {
                                scope.launch {
                                    delay(500)
                                    items = items.toMutableList().apply { remove(item) }
                                }
                                true
                            }

                            else -> false
                        }
                    },
                    positionalThreshold = { totalDistance -> totalDistance * .5F },
                )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(end = maxHeight / 2 - (Icons.Default.Delete.defaultWidth / 2)),
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    content = {
                        ListItem(
                            headlineContent = { Text(item.text) },
                            supportingContent = { Text(item.secondaryText) },
                            overlineContent = { Text(item.overlineText) },
                        )
                    }
                )
            }
        }
    }
}

@PreviewDefault
@Composable
private fun SwipableContentPreview() {
    AppTheme { SwipableContent() }
}

private val testItems: List<TestItem>
    get() {
        return (1..10).toList().map { TestItem(it, "Test$it", "Secondary Text$it", "Overline Text$it") }
    }

private data class TestItem(val id: Int, val text: String, val secondaryText: String, val overlineText: String)