package com.example.template.ui.composable

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AppModalDrawerSheet(
    drawerState: DrawerState,
    onDismissRequest: () -> Unit,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    content: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val normalLayout = LocalLayoutDirection.current

    if (drawerState.isOpen) {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            // Wrap in box so that the ModalDrawerSheet sits tight against the right side.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            scope.launch {
                                drawerState.close()
                                onDismissRequest()
                            }
                        }
                    }
            ) {
                ModalDrawerSheet(
                    modifier = Modifier.safeDrawingPadding(), // Prevents drawing under system bars
                    drawerState = drawerState,
                    drawerTonalElevation = 4.dp,
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides normalLayout) { content() }
                }
            }
        }
    }
}