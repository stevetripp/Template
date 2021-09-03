package com.example.template.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        Box(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState(pageCount = 3, initialOffscreenLimit = 3)
            val coroutineScope = rememberCoroutineScope()

            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) { page ->
                // Our page content
                Card(
                    Modifier
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
                        .fillMaxWidth(.3f)
                        .fillMaxHeight(.1f)
                        .clickable { coroutineScope.launch { pagerState.animateScrollToPage(page) } }
                        .padding(bottom = 12.dp),
                    backgroundColor = Color.Blue,
                ) {
                    Box(modifier = Modifier.fillMaxSize())
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