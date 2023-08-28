package com.example.template.ux.animatedgestures

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

private enum class State {
    HIDDEN,
    FADE_IN,
    MOVE_LEFT
}

@Composable
fun AnimatedGestureScreen(navController: NavController) {
    AnimatedGesturesContent(navController::popBackStack)
}

@Composable
fun AnimatedGesturesContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.ANIMATED_GESTURE.title, onBack = onBack) }) {
//        UpdateTransitionExample()
        RememberInfiniteTransitionExample()
    }
}

@Composable
private fun RememberInfiniteTransitionExample() {
    var isVisible by remember { mutableStateOf(true) }
    AnimatedVisibility(
        modifier = Modifier,
        visible = isVisible,
        enter = fadeIn()
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val transition = rememberInfiniteTransition()
            val alphaAnimatable by transition.animateFloat(
                initialValue = .5F,
                targetValue = 0F,
                animationSpec = infiniteRepeatable(tween(2000)),
                label = "alphaAnimatable",
            )
            val offsetAnimatable by transition.animateValue(
                initialValue = 0.dp,
                targetValue = -maxWidth,
                animationSpec = infiniteRepeatable(tween(2000)),
                typeConverter = Dp.VectorConverter,
                label = "offsetAnimatable",
            )
            GestureIndicator(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = offsetAnimatable)
                    .alpha(alphaAnimatable)
            )
        }
    }
    Button(
        onClick = { isVisible = !isVisible }
    ) {
        Text("isVisible: $isVisible")
    }
}

@Preview
@Composable
private fun AnimatedGestureContentPreview() {
    AppTheme { AnimatedGesturesContent() }
}

@Composable
private fun GestureIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color = AppTheme.colors.primary.copy(alpha = .5f), shape = CircleShape)
            .size(64.dp)
            .border(width = 2.dp, AppTheme.colors.onBackground.copy(alpha = .5f), shape = CircleShape)
    )
}

@Preview
@Composable
private fun GestureIndicatorPreview() {
    AppTheme { Surface() { GestureIndicator() } }
}
