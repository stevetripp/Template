package com.example.template.ux.breadcrumbs

import androidx.lifecycle.ViewModel

/**
 * Abstract ViewModel for managing breadcrumb navigation.
 *
 * Subclasses must provide a list of [BreadcrumbRoute]s representing the navigation path.
 *
 * Usage:
 * - Extend this class and override [breadcrumbRoutes] to supply the breadcrumb trail for your screen.
 *
 * Example:
 * ```kotlin
 * class MyScreenViewModel : BreadcrumbViewModel() {
 *     override val breadcrumbRoutes = listOf(
 *         BreadcrumbRoute("Home", ...),
 *         BreadcrumbRoute("Section", ...),
 *         BreadcrumbRoute("Current", ...)
 *     )
 * }
 * ```
 */
abstract class BreadcrumbViewModel : ViewModel() {
    /**
     * The list of breadcrumb routes representing the navigation path for the current screen.
     *
     * Subclasses must override this property to define the breadcrumb trail specific to their screen.
     * Each [BreadcrumbRoute] in the list represents a step in the navigation hierarchy, typically
     * ordered from the root (home) to the current screen.
     *
     * @see BreadcrumbRoute
     */
    abstract val breadcrumbRoutes: List<BreadcrumbRoute>
}