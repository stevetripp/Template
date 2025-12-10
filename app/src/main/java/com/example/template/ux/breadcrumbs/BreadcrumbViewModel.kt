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
 *
 * @property breadcrumbRoutes The list of breadcrumb routes for the current screen.
 */
abstract class BreadcrumbViewModel : ViewModel() {
    /**
     * The list of breadcrumb routes representing the navigation path.
     * Subclasses must override this property.
     */
    abstract val breadcrumbRoutes: List<BreadcrumbRoute>
}