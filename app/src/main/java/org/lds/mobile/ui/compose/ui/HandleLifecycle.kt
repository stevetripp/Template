package org.lds.mobile.ui.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Observes lifecycle changes which can be handled by implementing [onEvent].
 *
 * // Screen
 * HandleLifecycle { lifecycle, event ->  viewModel.HandleLifecycle(event) }
 *
 * // ViewModel
 * fun handleLifeCycleEvent(event: Lifecycle.Event) {
 *  when (event) {
 *          Lifecycle.Event.ON_RESUME -> { }
 *          Lifecycle.Event.ON_PAUSE -> { }
 *          else -> Unit
 *      }
 *  }
 */
@Composable
fun HandleLifecycle(onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit) {
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { lifecycleOwner, event -> onEvent(lifecycleOwner, event) }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
}