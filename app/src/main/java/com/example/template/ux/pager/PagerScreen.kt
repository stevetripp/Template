package com.example.template.ux.pager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@Composable
fun PagerScreen(navController: NavController) {
    PagerContent(navController::popBackStack)
}

@Composable
fun PagerContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.PAGER.title, onBack = onBack) }) { paddingValues ->
        val pagerState = rememberPagerState()
        Box(modifier = Modifier.padding(paddingValues)) {
            HorizontalPager(
                state = pagerState,
                count = 10
            ) { page ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Page: $page", modifier = Modifier.align(Alignment.Center))
                }
            }

            HorizontalPagerIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                pagerState = pagerState,
            )
        }
    }
}

@PreviewDefault
@Composable
private fun PagerPreview() {
    AppTheme { PagerContent() }
}