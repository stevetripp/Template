package org.lds.mobile.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

/**
 * Observes changes to the navigation back stack and invokes a callback when the current route changes.
 *
 * This composable tracks the back stack state across recompositions and detects when the current
 * route (last item in the back stack) changes. When a change is detected, it invokes the
 * [onRouteChanged] callback with the new current route.
 *
 * @param currentBackStack The navigation back stack to observe
 * @param ignoreBack If true, skips notification when navigating backwards through the stack.
 *                   Only notifies on forward navigation. Defaults to false.
 * @param onRouteChanged Callback invoked with the current route when it changes
 *
 * @example
 * ```kotlin
 * backstack?.let {
 *     ObserveRouteChanges(it) { navKey ->
 *         Logger.i("""Navigating to: $navKey""")
 *     }
 * }
 * ```
 */
@Composable
fun ObserveRouteChanges(currentBackStack: NavBackStack<NavKey>, ignoreBack: Boolean = false, onRouteChanged: (NavKey) -> Unit) {
    // Track the previous back stack state to detect navigation changes
    val previousBackStack = rememberNavBackStack()

    // Early exit if back stack hasn't changed
    if (previousBackStack == currentBackStack) return

    // Determine if user is navigating backwards through the stack (before updating state)
    val navigatingBack = previousBackStack.size > currentBackStack.size

    // Update tracked state with current back stack for next comparison
    previousBackStack.clear()
    previousBackStack.addAll(currentBackStack)

    // Skip callback if we should ignore back navigation
    if (ignoreBack && navigatingBack) return

    // Get the most recent route from the back stack
    val currentRoute = currentBackStack.last()

    // Notify via callback when the current route reference changes
    LaunchedEffect(currentRoute) {
        onRouteChanged(currentRoute)
    }
}