package com.example.template.ui.composable

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer

/**
 * [Composable] that provides panning and zoom functionality for [content]
 */
@Composable
fun PanAndZoom(modifier: Modifier = Modifier, scalingLimit: ClosedRange<Float> = (.5F..4F), content: @Composable (BoxWithConstraintsScope.(scale: Float) -> Unit)) {

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(scalingLimit)
        offset += offsetChange
    }
    BoxWithConstraints(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x * scale,
                translationY = offset.y * scale
            )
            .transformable(state = state)
    ) {
        content(scale)
    }
}
