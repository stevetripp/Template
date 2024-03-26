package com.example.template.ux.navigatepager

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.R
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import com.google.android.material.math.MathUtils
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun NavigationPagerScreen(navController: NavController) {
    NavigationPagerContent(navController::popBackStack)
}

@Composable
fun NavigationPagerContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.NAVIGATION_PAGER.title, onBack = onBack) }) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            NavigationPager(
                items = listOf(
                    NavigationItem(painterResource(id = R.drawable.coloring_book)),
                    NavigationItem(painterResource(id = R.drawable.coloring_book)),
                    NavigationItem(painterResource(id = R.drawable.coloring_book)),
                    NavigationItem(painterResource(id = R.drawable.coloring_book)),
                    NavigationItem(painterResource(id = R.drawable.coloring_book)),
                ),
                initialItemInFocus = 1,
                onItemFocused = { Log.i("SMT", "onItemFocused: $it") },
                onItemSelected = { Log.i("SMT", "onItemSelected: $it") },
            )
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.NavigationItem(painter: Painter, modifier: Modifier = Modifier): @Composable () -> Unit {
    return {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier
                .align(Alignment.Center)
                .fillMaxSize()
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.NavigationPager(
    items: List<@Composable () -> Unit>,
    initialItemInFocus: Int,
    modifier: Modifier = Modifier,
    onItemFocused: (index: Int) -> Unit,
    onItemSelected: (index: Int) -> Unit,
) {
    val pagerState = rememberPagerState { items.size }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect() {
            onItemFocused(it)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    HorizontalPager(
        modifier = modifier
            .height(120.dp),
        state = pagerState,
        contentPadding = PaddingValues(horizontal = maxWidth / 3)
    ) { page ->
        // Our page content
        Box(
            Modifier
                .fillMaxHeight(0.8f)
                .aspectRatio(1.0f, true)
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                    val percentage = pageOffset.coerceIn(0f, 2f) / 2f

                    // We animate the scaleX + scaleY, between 85% and 100%
                    MathUtils
                        .lerp(1f, .5f, percentage)
                        .also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                    // We animate the alpha, between 50% and 100%
                    alpha = MathUtils.lerp(1f, .0f, percentage)
                }
                .clickable {
                    Log.i(
                        "SMT", """
                        |page: $page
                        |currentPage: ${pagerState.currentPage}""".trimIndent()
                    )
                    if (page != pagerState.currentPage) {
                        coroutineScope.launch { pagerState.animateScrollToPage(page) }
                    } else {
                        onItemSelected(page)
                    }
                }
        ) {
            items[page].invoke()
        }
    }
}

@PreviewDefault
@Composable
private fun NavigationPagerContentPreview() {
    AppTheme { NavigationPagerContent() }
}