package com.example.template.util

import com.example.template.util.DeepLink.HOST
import com.example.template.util.DeepLink.PATH_PREFIX
import com.example.template.util.DeepLink.SCHEME


/**
 * Constants for the [androidx.navigation.NavDeepLink] URIs
 *
 * The [SCHEME], [HOST], and [PATH_PREFIX] should match the manifest intent-filter data elements respectively
 */
object DeepLink {
    private const val SCHEME = "https"
    private const val HOST = "trippntechnology.com"
    private const val PATH_PREFIX = "/template"

    const val ROOT = "$SCHEME://$HOST$PATH_PREFIX"
}