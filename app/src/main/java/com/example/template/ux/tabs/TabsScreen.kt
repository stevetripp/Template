package com.example.template.ux.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ux.main.Screen
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState

@Composable
fun TabsScreen(navController: NavController) {
    TabsContent(navController::popBackStack)
}

@Composable
fun TabsContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.TABS.title, onBack = onBack) }) { paddingValues ->
        var tabIndex by remember { mutableStateOf(0) }
        val tabTitles = listOf("Hello", "There", "World")
        val pagerState = rememberPagerState()
        Column {
            TabRow(selectedTabIndex = tabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index,
                        onClick = {
                            tabIndex = index
                        },
                        text = { Text(text = title) })
                }
            }
            HorizontalPager(
                count = tabTitles.size,
                state = pagerState,
            ) { tabIndex ->
                Text(
                    tabIndex.toString(),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        LaunchedEffect(key1 = tabIndex) { pagerState.animateScrollToPage(tabIndex) }
    }
}

@Composable
private fun CustomTab(title: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        Modifier
            .padding(10.dp)
            .height(50.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            Modifier
                .size(10.dp)
                .align(Alignment.CenterHorizontally)
                .background(color = if (selected) Color.Red else Color.White)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@PreviewDefault
@Composable
private fun TabsContentPreview() {
    AppTheme { TabsContent() }
}