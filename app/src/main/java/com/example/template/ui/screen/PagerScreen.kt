package com.example.template.ui.screen

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.template.AppBar
import com.example.template.Nav
import com.example.template.ui.theme.AppTheme
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@Composable
fun PagerScreen(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        val pagerState = rememberPagerState()
        Box {
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

@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Preview(group = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Composable
private fun PagerPreview() {
    AppTheme { PagerScreen(nav = Nav.PAGER, onBack = {}) }
}