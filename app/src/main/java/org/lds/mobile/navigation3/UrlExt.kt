package org.lds.mobile.navigation3

import androidx.navigation3.runtime.NavKey
import io.ktor.http.Url
import kotlinx.serialization.json.Json

/**
 * Checks if this URL matches the given URL pattern.
 *
 * Path segments enclosed in curly braces (e.g., `{id}`) are treated as parameter
 * placeholders that match any value. All other segments must match exactly.
 *
 * @param patternUrl The URL pattern to match against. Parameter placeholders are
 *                   defined using curly braces (e.g., `{userId}`)
 * @return `true` if this URL matches the pattern; `false` if the path structure
 *         differs or segment values don't match
 *
 * @example
 * ```
 * val url = Url("https://example.com/users/42/posts/5")
 * val pattern = Url("https://example.com/users/{userId}/posts/{postId}")
 * url.matches(pattern) // returns true
 * ```
 */
fun Url.matches(patternUrl: Url): Boolean {
    val placeholderPattern = "\\{.*?\\}".toRegex()
    val patternSegments = patternUrl.segments.map { it.replace(placeholderPattern, ".*") }
    val targetSegments = this.segments

    if (patternSegments.size != targetSegments.size) return false

    patternSegments.forEachIndexed { index, value ->
        if (!targetSegments[index].matches(value.toRegex())) return false
    }

    return true
}

/**
 * Extracts path and query parameters from this URL based on the given pattern.
 *
 * Extracts path parameters (defined in curly braces in the pattern) and all query
 * parameters from the URL. Returns an empty map if the URL doesn't match the pattern.
 *
 * @param patternUrl The URL pattern used to extract path parameters. Path parameters
 *                   are defined using curly braces (e.g., `{id}`)
 * @return A map containing both path and query parameter names to their string values.
 *         Returns an empty map if the URL structure doesn't match the pattern
 *
 * @example
 * ```
 * val url = Url("https://example.com/items/123?name=test&category=books")
 * val pattern = Url("https://example.com/items/{id}")
 *
 * val params = url.getParameters(pattern)
 * // params == mapOf("id" to "123", "name" to "test", "category" to "books")
 * ```
 */
fun Url.getParameters(patternUrl: Url): Map<String, String> {

    if (!matches(patternUrl)) return emptyMap()

    val results = mutableMapOf<String, String>()

    // Extract all query parameters from the URL
    this.parameters.forEach { key, values ->
        values.firstOrNull()?.let { results[key] = it }
    }

    // Extract path parameters by matching pattern placeholders to segment values
    val patternSegments = patternUrl.segments
    val targetSegments = this.segments

    patternSegments.forEachIndexed { index, patternSegment ->
        if (patternSegment.startsWith("{") && patternSegment.endsWith("}")) {
            val paramName = patternSegment.substring(1, patternSegment.length - 1)
            results[paramName] = targetSegments[index]
        }
    }

    return results
}

/**
 * Converts extracted URL parameters to a typed navigation route object.
 *
 * Extracts parameters based on the given pattern URL and deserializes them into
 * the specified navigation route type using JSON serialization. This is useful for
 * converting deep link parameters into type-safe route objects.
 *
 * @param T The target navigation route type that extends [NavKey]
 * @param patternUrl The URL pattern used to extract parameters
 * @return An instance of type T populated with the extracted parameters
 * @throws kotlinx.serialization.SerializationException if parameters cannot be
 *         deserialized into type T
 *
 * @example
 * ```
 * @Serializable
 * data class ItemDetailsRoute(val id: String) : NavKey
 *
 * val url = Url("https://example.com/items/123")
 * val pattern = Url("https://example.com/items/{id}")
 *
 * val route = url.toRoute<ItemDetailsRoute>(pattern)
 * // route.id == "123"
 * ```
 */
inline fun <reified T : NavKey> Url.toRoute(patternUrl: Url): T {
    val parameters = getParameters(patternUrl)
    val jsonString = Json.encodeToString(parameters)
    return Json.decodeFromString<T>(jsonString)
}