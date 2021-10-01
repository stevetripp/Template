package com.example.template.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.template.AppBar
import com.example.template.Nav
import com.example.template.ui.theme.AppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.math.MathUtils
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavigationPager(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val colors = mapOf(0 to Color.Blue, 1 to Color.Red, 2 to Color.Green)
    val pagerState = rememberPagerState(1)
    val coroutineScope = rememberCoroutineScope()
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(
                modifier = Modifier
                    .height(100.dp),
                count = 3,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 130.dp)
            ) { page ->
                // Our page content
                Card(
                    Modifier
                        .width(100.dp)
                        .graphicsLayer {
                            // Calculate the absolute offset for the current page from the
                            // scroll position. We use the absolute value which allows us to mirror
                            // any effects for both directions
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                            val percentage = pageOffset.coerceIn(0f, 2f) / 2f

                            // We animate the scaleX + scaleY, between 85% and 100%
                            MathUtils
                                .lerp(1f, .5f, percentage)
                                .also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }

                            // We animate the alpha, between 50% and 100%
                            alpha = MathUtils.lerp(1f, .5f, percentage)
                        }
                        .fillMaxHeight(.6f)
                        .clickable { coroutineScope.launch { pagerState.animateScrollToPage(page) } },
                    backgroundColor = colors[page]!!,
                ) {
                    Box(/*modifier = Modifier.background(Color.LightGray)*/)
                    {
                        Text(
                            text = "Page: $page",
                            modifier = Modifier
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun NavigationPagerPreview() {
    AppTheme {
        NavigationPager(nav = Nav.PAGER, onBack = {})
    }
}