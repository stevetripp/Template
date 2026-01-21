package com.example.template.util

/**
 * Constants for deep link URIs used in Android manifest intent-filters.
 *
 * Defines the base deep link configuration supporting both HTTPS app links and custom URI schemes.
 */
object DeepLinkConstants {
    const val HOST = "trippntechnology.com"
    const val PATH_PREFIX = "template"
    const val SCHEME_CUSTOM = "myscheme"
    const val SCHEME_HTTPS = "https"

    const val CUSTOM_ROOT = "$SCHEME_CUSTOM://$PATH_PREFIX"
    const val HTTPS_ROOT = "$SCHEME_HTTPS://$HOST"
}
