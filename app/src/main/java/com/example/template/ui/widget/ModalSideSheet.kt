package com.example.template.ui.widget

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.theme.AppTheme

/**
 * [ModalSideSheet] mimics a ModalBottomSheet.
 */
@Composable
fun ModalSideSheet(
    onDismissRequest: () -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    content: @Composable () -> Unit,
) {
    // This variable is used to force the ModelSideSheet to show when it is initially composed so that the surfaceWidth can be calculated
    var showOnInitialComposition by remember { mutableStateOf(true) }
    // This is set by the Surface when it is initially displayed and is used to animate the side sheet visibility
    var surfaceWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    // Animated offset to slide in/out the side sheet
    val offset by animateDpAsState(targetValue = if (isExpanded) 0.dp else surfaceWidth, label = "")

    if (showOnInitialComposition || isExpanded || offset != surfaceWidth) {
        showOnInitialComposition = false
        Box(modifier = modifier
            .fillMaxSize()
            .background(scrimColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Disable ripple effect
            ) { onDismissRequest() }
        )
        {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(alignment = Alignment.CenterEnd)
                    .clickable(enabled = false) { } // Prevent tap to propagate thru Surface
                    .clip(shape)
                    .onGloballyPositioned { coordinates ->
                        surfaceWidth = with(density) { coordinates.size.width.toDp() }
                    }
                    .offset {
                        IntOffset(
                            offset
                                .toPx()
                                .toInt(), 0
                        )
                    },
                content = content
            )
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    var isExpanded by remember { mutableStateOf(false) }

    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            TextButton(onClick = { isExpanded = true }) { Text(text = "Expand") }
        }

        ModalSideSheet(
            onDismissRequest = { isExpanded = false },
            isExpanded = isExpanded,
            content = {
                Text(text = "Side Sheet Content")
            }
        )
    }
}