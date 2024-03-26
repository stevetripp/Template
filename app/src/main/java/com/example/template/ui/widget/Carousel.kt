package com.example.template.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val MAX_CAROUSEL_ITEMS = 30000

@Composable
fun Carousel(itemCount: Int, modifier: Modifier = Modifier, content: @Composable PagerScope.(itemIndex: Int) -> Unit) {
    Box(modifier = modifier) {
        val contentPadding = if (itemCount > 1) 24.dp else 8.dp

        val pagerState = rememberPagerState(
            initialPage = (MAX_CAROUSEL_ITEMS / 2) - ((MAX_CAROUSEL_ITEMS / 2) % itemCount),
            pageCount = { if (itemCount == 1) 1 else MAX_CAROUSEL_ITEMS }
        )

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = contentPadding),
        ) { page ->
            content(page % itemCount)
        }
    }
}