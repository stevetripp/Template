package com.example.template.ux.panningzooming

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ext.findBestFit
import com.example.template.ui.PreviewPhoneOrientations
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

@Composable
fun PanningZoomingScreen(navController: NavController) {
    PanningZoomingContent(navController::popBackStack)
}

@Composable
fun PanningZoomingContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.PANNING_ZOOMING.title, onBack = onBack) }) {
        val outlineStream = LocalContext.current.assets.open("CP011-Outline-iPad.png")
        val pageImage = BitmapFactory.decodeStream(outlineStream).asImageBitmap()

        CanvasCard(modifier = Modifier.padding(it), pageImage = pageImage)
    }
}

@Composable
fun CanvasCard(pageImage: ImageBitmap, modifier: Modifier = Modifier) {

    var tapOffset by remember { mutableStateOf(Offset(0F, 0F)) }

    PanAndZoom(
        modifier = modifier
            .fillMaxSize()
    ) {
        val pageWidthPx = pageImage.width
        val pageHeightPx = pageImage.height
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }

        val paddingDp = 5.dp
        val paddingPx = with(LocalDensity.current) { paddingDp.toPx() }
        val cardSize = IntSize(pageWidthPx, pageHeightPx).findBestFit(maxWidthPx, maxHeightPx, paddingPx)
        val pageWidthDp = with(LocalDensity.current) { cardSize.width.toDp() }
        val pageHeightDp = with(LocalDensity.current) { cardSize.height.toDp() }

        Card(
            modifier = Modifier
                .width(pageWidthDp)
                .height(pageHeightDp)
                .shadow(5.dp)
                .align(Alignment.Center)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White), painter = BitmapPainter(pageImage), contentDescription = null
            )
        }
    }
}

@PreviewPhoneOrientations
@Composable
private fun CanvasCardPreview() {
    AppTheme { PanningZoomingContent() }
}