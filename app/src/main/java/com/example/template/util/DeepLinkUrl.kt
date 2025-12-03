package com.example.template.util

import androidx.navigation3.runtime.NavKey
import io.ktor.http.Url
import kotlinx.serialization.json.Json

/**
 * Represents a deep link URL and provides utilities for matching against URL patterns
 * and extracting parameters.
 *
 * This class wraps a raw URL string and provides functionality to:
 * - Match the URL against URL patterns with path parameters (e.g., "{id}")
 * - Extract both path and query parameters from the URL
 * - Convert extracted parameters to navigation route objects
 *
 * @param rawUrl The raw URL string to be parsed and matched
 *
 * @example
 * ```
 * val deepLink = DeepLinkUrl("https://example.com/items/123?name=test")
 * val pattern = Url("https://example.com/items/{id}")
 *
 * if (deepLink.matches(pattern)) {
 *     val params = deepLink.getParameters(pattern)
 *     // params["id"] = "123", params["name"] = "test"
 * }
 * ```
 */
data class DeepLinkUrl(val rawUrl: String) {

    private val _ktorUrl: Url = Url(rawUrl)

    /**
     * Checks if this URL matches the given URL pattern.
     *
     * Path segments enclosed in curly braces (e.g., "{id}") in the pattern are treated
     * as parameter placeholders and will match any value. All other segments must match
     * exactly, and the number of path segments must be identical.
     *
     * @param patternUrl The URL pattern to match against
     * @return `true` if this URL matches the pattern, `false` otherwise
     *
     * @example
     * ```
     * val deepLink = DeepLinkUrl("https://example.com/users/42/posts/5")
     * val pattern = Url("https://example.com/users/{userId}/posts/{postId}")
     * deepLink.matches(pattern) // returns true
     * ```
     */
    fun matches(patternUrl: Url): Boolean {
        val patternPath = patternUrl.segments.map { it.replace("\\{.*?\\}".toRegex(), ".*") }
        val targetPath = _ktorUrl.segments

        if (patternPath.size != targetPath.size) return false

        patternPath.forEachIndexed { index, value ->
            if (!targetPath[index].matches(value.toRegex())) return false
        }

        return true
    }

    /**
     * Extracts parameters from this URL based on the given URL pattern.
     *
     * This method extracts both path parameters (defined in curly braces in the pattern)
     * and query parameters from the URL. The URL must match the pattern for extraction
     * to succeed.
     *
     * @param patternUrl The URL pattern used to extract path parameters
     * @return A map of parameter names to their string values. Returns an empty map if
     *         the URL does not match the pattern.
     *
     * @example
     * ```
     * val deepLink = DeepLinkUrl("https://example.com/items/123?name=test&category=books")
     * val pattern = Url("https://example.com/items/{id}")
     *
     * val params = deepLink.getParameters(pattern)
     * // params == mapOf("id" to "123", "name" to "test", "category" to "books")
     * ```
     */
    fun getParameters(patternUrl: Url): Map<String, String> {

        if (!matches(patternUrl)) return emptyMap()

        val results = mutableMapOf<String, String>()

        // Extract query parameters from the stored URL
        _ktorUrl.parameters.forEach { key, values ->
            values.firstOrNull()?.let { results[key] = it }
        }

        // Match and extract path parameters
        val patternSegments = patternUrl.segments
        val targetSegments = _ktorUrl.segments

        // Only process if path segments match in count
        if (patternSegments.size == targetSegments.size) {
            patternSegments.forEachIndexed { index, patternSegment ->
                if (patternSegment.startsWith("{") && patternSegment.endsWith("}")) {
                    val paramName = patternSegment.substring(1, patternSegment.length - 1)
                    results[paramName] = targetSegments[index]
                }
            }
        }

        return results
    }

    /**
     * Converts the extracted parameters from a URL to a navigation route object.
     *
     * This method extracts parameters based on the given pattern URL and deserializes them
     * into the specified navigation route type using JSON serialization. This is useful for
     * converting deep link parameters into type-safe route objects.
     *
     * @param T The target navigation route type that extends [NavKey]
     * @param patternUrl The URL pattern used to extract parameters
     * @return An instance of type T populated with the extracted parameters
     * @throws kotlinx.serialization.SerializationException if the parameters cannot be
     *         deserialized into type T
     *
     * @example
     * ```
     * @Serializable
     * data class ItemDetailsRoute(val id: String) : NavKey
     *
     * val deepLink = DeepLinkUrl("https://example.com/items/123")
     * val pattern = Url("https://example.com/items/{id}")
     *
     * val route = deepLink.toRoute<ItemDetailsRoute>(pattern)
     * // route.id == "123"
     * ```
     */
    inline fun <reified T : NavKey> toRoute(patternUrl: Url): T {
        val parameters = getParameters(patternUrl)
        val jsonString = Json.Default.encodeToString(parameters)
        return Json.Default.decodeFromString<T>(jsonString)
    }
}