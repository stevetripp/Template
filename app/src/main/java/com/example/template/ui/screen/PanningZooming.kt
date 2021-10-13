package com.example.template.ui.screen

import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.template.AppBar
import com.example.template.Nav
import com.example.template.ext.findBestFit

@Composable
fun PanningZooming(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Scaffold(topBar = { AppBar(nav, onBack) }) {

        val outlineStream = LocalContext.current.assets.open("CP011-Outline-iPad.png")
        val page = BitmapFactory.decodeStream(outlineStream).asImageBitmap()
//        val maskStream = LocalContext.current.assets.open("CP011-Mask-iPad.png")
//        val maskBitmap = BitmapFactory.decodeStream(maskStream).asImageBitmap()

        CanvasCard(pageImage = page)
    }
}

@Composable
fun CanvasCard(pageImage: ImageBitmap) {

    var tapOffset by remember { mutableStateOf(Offset(0F, 0F)) }

    BoxWithPanZoom(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val pageWidthPx = pageImage.width
        val pageHeightPx = pageImage.height
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }

        val paddingDp = 5.dp
        val paddingPx = with(LocalDensity.current) { paddingDp.toPx() }
        val cardSize = IntSize(pageWidthPx, pageHeightPx).findBestFit(maxWidthPx, maxHeightPx, paddingPx)
        val pageWidthDp = with(LocalDensity.current) { cardSize.width.toDp() /*pageWidthPx.toDp()*/ }
        val pageHeightDp = with(LocalDensity.current) { cardSize.height.toDp() /*pageHeightPx.toDp()*/ }

        Card(
            modifier = Modifier
                .width(pageWidthDp)
                .height(pageHeightDp)
                .shadow(5.dp)
                .align(Alignment.Center)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectTapGestures { tapOffset = it }
                    }
            ) {
                drawImage(image = pageImage, dstSize = IntSize(size.width.toInt(), size.height.toInt()))
            }
            Text(text = tapOffset.toString(), color = Color.Gray)
        }
    }
}

@Composable
private fun CanvasCardPreview() {
    val outlineStream = LocalContext.current.assets.open("CP011-Outline-iPad.png")
    val page = BitmapFactory.decodeStream(outlineStream).asImageBitmap()
    CanvasCard(pageImage = page)
}

@Preview
@Composable
private fun CanvasCardPreviewNarrow() = CanvasCardPreview()

@Preview(widthDp = 1024, heightDp = 300)
@Composable
private fun CanvasCardPreviewWide() = CanvasCardPreview()

@Composable
private fun BoxWithPanZoom(modifier: Modifier = Modifier, content: @Composable BoxWithConstraintsScope.() -> Unit) {
    var scale by remember { mutableStateOf(1F) }
    var offset by remember { mutableStateOf(Offset(0F, 0F)) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(.7F, 4F)
        offset += offsetChange
    }

    BoxWithConstraints(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y,
            )
            .transformable(state = state)
    ) {
        content()
    }
}