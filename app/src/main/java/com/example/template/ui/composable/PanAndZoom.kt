package com.example.template.ui.composable

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput

/**
 * [Composable] that provides panning and zoom functionality for [content]
 */
@Composable
fun PanAndZoom(
    modifier: Modifier = Modifier,
    scalingLimit: ClosedRange<Float> = (.5F..4F),
    content: @Composable (BoxWithConstraintsScope.(scale: Float) -> Unit)
) {

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    BoxWithConstraints(
        modifier = modifier
            .pointerInput(Unit) {
                detectTransformGestures(
                    consume = false,
                    pass = PointerEventPass.Initial,
                    onGesture = { centroid, pan, gestureZoom, _, _, changes ->
                        val oldScale = scale
                        val newScale = (scale * gestureZoom).coerceIn(scalingLimit)
                        val isTwoFingerGesture = changes.size >= 2
                        if (isTwoFingerGesture) {
                            offset = (offset + centroid / oldScale) - (centroid / newScale + pan / oldScale)
                            scale = newScale
                            //Consume here so lifting one finger while zooming/panning does NOT count as another gesture
                            changes.forEach { it.consume() }
                        }
                    },
                )
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = -offset.x * scale,
                translationY = -offset.y * scale,
                transformOrigin = TransformOrigin(0f, 0f)
            )
    ) {
        content(scale)
    }
}