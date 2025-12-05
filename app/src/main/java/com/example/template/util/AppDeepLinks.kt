package com.example.template.util

/**
 * Constants for deep link URIs used in Android manifest intent-filters.
 *
 * These constants define the base deep link configuration for the application.
 * The [SCHEME], [HOST], and [PATH_PREFIX] values should match the corresponding
 * data elements defined in the manifest's intent-filter.
 *
 * @property SCHEME The URI scheme (protocol) for deep links (e.g., "https")
 * @property HOST The host/domain for deep links (e.g., "trippntechnology.com")
 * @property PATH_PREFIX The base path prefix for all application deep links (e.g., "/template")
 * @property ROOT The complete base URI combining scheme, host, and path prefix
 */
object AppDeepLinks {
    private const val SCHEME = "https"
    private const val HOST = "trippntechnology.com"
    private const val PATH_PREFIX = "/template"

    /**
     * The complete base URI for all application deep links.
     *
     * Format: `{SCHEME}://{HOST}{PATH_PREFIX}`
     * Example: "https://trippntechnology.com/template"
     */
    const val ROOT = "$SCHEME://$HOST$PATH_PREFIX"
}
