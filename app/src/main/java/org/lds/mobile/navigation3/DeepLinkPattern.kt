package org.lds.mobile.navigation3

import io.ktor.http.Url

/**
 * Represents a deep link URL pattern used for matching and extracting parameters from incoming deep links.
 *
 * Patterns can contain dynamic segments enclosed in curly braces (e.g., `{userId}`, `{postId}`)
 * which serve as placeholders that match any value in those positions. These dynamic segments
 * are extracted as named parameters when matching against actual deep link URIs.
 *
 * @property url The [Url] object representing the deep link pattern
 *
 * @example
 * ```
 * // Pattern with dynamic path segments
 * val pattern = DeepLinkPattern(Url("https://example.com/users/{userId}/posts/{postId}"))
 *
 * // Matching against an actual deep link
 * val deepLink = DeepLink(Url("https://example.com/users/123/posts/456"))
 * if (deepLink.matches(pattern)) {
 *     val params = deepLink.getParameters(pattern)
 *     // params == mapOf("userId" to "123", "postId" to "456")
 * }
 * ```
 */
data class DeepLinkPattern(val url: Url)