package com.example.template.util.ext

import io.ktor.http.Url
import org.lds.mobile.navigation.RouteUtil
import org.lds.mobile.navigation3.DeepLinkPattern

/**
 * Adds literal path segments to this deep link pattern.
 *
 * @param argNames The path segment names to append to the URL path
 * @return A new [DeepLinkPattern] with the additional path segments appended
 *
 * @example
 * ```
 * DeepLinkPattern(Url("https://example.com"))
 *     .addPathSegments("users", "profile")
 * // Results in: https://example.com/users/profile
 * ```
 */
fun DeepLinkPattern.addPathSegments(vararg argNames: String): DeepLinkPattern {
    val segments = argNames.joinToString("/", "/")
    return DeepLinkPattern(Url(url.toString() + segments))
}

/**
 * Adds variable path segments to this deep link pattern.
 *
 * Path segment variables are defined using the [RouteUtil.defineArg] function,
 * which creates placeholder arguments that can be extracted from the deep link URL.
 *
 * @param argNames The names of the path segment variables to add
 * @return A new [DeepLinkPattern] with the variable path segments appended
 *
 * @example
 * ```
 * DeepLinkPattern(Url("https://example.com"))
 *     .addPathSegmentVariables("userId", "postId")
 * // Results in: https://example.com/{userId}/{postId}
 * ```
 */
fun DeepLinkPattern.addPathSegmentVariables(vararg argNames: String): DeepLinkPattern {
    val segments = argNames.joinToString("/", "/") { argName -> RouteUtil.defineArg(argName) }
    return DeepLinkPattern(Url(url.toString() + segments))
}

/**
 * Adds optional query parameter variables to this deep link pattern.
 *
 * Query parameter variables are defined using [RouteUtil.defineOptionalArgs] function,
 * which creates optional parameters that can be extracted from the deep link URL.
 *
 * @param argNames The names of the query parameter variables to add
 * @return A new [DeepLinkPattern] with the optional query parameters appended
 *
 * @example
 * ```
 * DeepLinkPattern(Url("https://example.com"))
 *     .addQueryParameterVariables("filter", "sort", "page")
 * // Results in: https://example.com?filter=...&sort=...&page=...
 * ```
 */
fun DeepLinkPattern.addQueryParameterVariables(vararg argNames: String): DeepLinkPattern {
    val parameters = argNames.joinToString("&", "?") { argName -> RouteUtil.defineOptionalArgs(argName) }
    return DeepLinkPattern(Url(url.toString() + parameters))
}