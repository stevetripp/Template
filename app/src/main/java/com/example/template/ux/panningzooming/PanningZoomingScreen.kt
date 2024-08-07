package com.example.template.ux.panningzooming

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ext.findBestFit
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.composable.PanAndZoom
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
            .background(Color.White)
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
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .pointerInteropFilter { motionEvent ->
                        Log.i("SMT", "motionEvent: $motionEvent")
                        true
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {

                            },
                            onLongPress = {

                            },
                            onPress = {
                                Log.i("SMT", "Pressed")
                                this.awaitRelease()
                                Log.i("SMT", "Released")
                            },
                            onTap = { tapOffset = it }
                        )
                        this.detectDragGestures(
                            onDragStart = { Log.i("SMT", "onDragStart: $it") },
                            onDragEnd = { Log.i("SMT", "onDragEnd") },
                            onDrag = { change, dragAmount ->
                                Log.i(
                                    "SMT", """onDrag
                                    |$change
                                    |$dragAmount
                                """.trimMargin()
                                )
                            }

                        )
                        detectHorizontalDragGestures { change, dragAmount ->
                            Log.i(
                                "SMT", """onDrag
                                    |$change
                                    |$dragAmount
                                """.trimMargin()
                            )
                        }
                        detectVerticalDragGestures { change, dragAmount ->
                            Log.i(
                                "SMT", """onDrag
                                    |$change
                                    |$dragAmount
                                """.trimMargin()
                            )
                        }
                    }
            ) {
                drawImage(image = pageImage, dstSize = IntSize(size.width.toInt(), size.height.toInt()))
            }
            Text(text = tapOffset.toString(), color = Color.Gray)
        }
    }
}

@Preview(group = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Preview(group = "Light", widthDp = 800, heightDp = 400, uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Preview(group = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Composable
private fun CanvasCardPreview() {
    val outlineStream = LocalContext.current.assets.open("CP011-Outline-iPad.png")
    val page = BitmapFactory.decodeStream(outlineStream).asImageBitmap()
    AppTheme { CanvasCard(pageImage = page) }
}