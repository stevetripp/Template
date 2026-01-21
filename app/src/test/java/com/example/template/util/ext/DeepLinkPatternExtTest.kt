package com.example.template.util.ext

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.ktor.http.Url
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.lds.mobile.navigation3.DeepLinkPattern

/**
 * Unit tests for the DeepLinkPattern extension functions.
 *
 * Tests verify the correct construction of deep link patterns with literal path segments,
 * variable path segments, and optional query parameters.
 */
class DeepLinkPatternExtTest {

    private lateinit var basePattern: DeepLinkPattern

    @BeforeEach
    fun setUp() {
        basePattern = DeepLinkPattern(Url("https://example.com"))
    }

    // ==================== addPathSegments Tests ====================

    @Test
    fun `addPathSegments with single segment appends correctly`() {
        val result = basePattern.addPathSegments("users")
        assertThat(result.url.toString()).isEqualTo("https://example.com/users")
    }

    @Test
    fun `addPathSegments with multiple segments appends correctly`() {
        val result = basePattern.addPathSegments("users", "profile")
        assertThat(result.url.toString()).isEqualTo("https://example.com/users/profile")
    }

    @Test
    fun `addPathSegments with three segments appends correctly`() {
        val result = basePattern.addPathSegments("api", "v1", "users")
        assertThat(result.url.toString()).isEqualTo("https://example.com/api/v1/users")
    }

    @Test
    fun `addPathSegments on pattern with existing path appends correctly`() {
        val patternWithPath = DeepLinkPattern(Url("https://example.com/api"))
        val result = patternWithPath.addPathSegments("users", "123")
        assertThat(result.url.toString()).isEqualTo("https://example.com/api/users/123")
    }

    @Test
    fun `addPathSegments returns new DeepLinkPattern instance`() {
        val result = basePattern.addPathSegments("test")
        assertThat(result === basePattern).isFalse()
    }

    @Test
    fun `addPathSegments with empty varargs appends only slash`() {
        val result = basePattern.addPathSegments()
        assertThat(result.url.toString()).isEqualTo("https://example.com/")
    }

    // ==================== addPathSegmentVariables Tests ====================

    @Test
    fun `addPathSegmentVariables with single variable appends correctly`() {
        val result = basePattern.addPathSegmentVariables("userId")
        val expectedUrl = "https://example.com/{userId}"
        assertThat(result.url.toString()).isEqualTo(expectedUrl)
    }

    @Test
    fun `addPathSegmentVariables with multiple variables appends correctly`() {
        val result = basePattern.addPathSegmentVariables("userId", "postId")
        val expectedUrl = "https://example.com/{userId}/{postId}"
        assertThat(result.url.toString()).isEqualTo(expectedUrl)
    }

    @Test
    fun `addPathSegmentVariables with three variables appends correctly`() {
        val result = basePattern.addPathSegmentVariables("tenantId", "userId", "resourceId")
        val expectedUrl = "https://example.com/{tenantId}/{userId}/{resourceId}"
        assertThat(result.url.toString()).isEqualTo(expectedUrl)
    }

    @Test
    fun `addPathSegmentVariables on pattern with existing path appends correctly`() {
        val patternWithPath = DeepLinkPattern(Url("https://example.com/api/v1"))
        val result = patternWithPath.addPathSegmentVariables("userId", "postId")
        val expectedUrl = "https://example.com/api/v1/{userId}/{postId}"
        assertThat(result.url.toString()).isEqualTo(expectedUrl)
    }

    @Test
    fun `addPathSegmentVariables returns new DeepLinkPattern instance`() {
        val result = basePattern.addPathSegmentVariables("test")
        assertThat(result === basePattern).isFalse()
    }

    @Test
    fun `addPathSegmentVariables with empty varargs appends only slash`() {
        val result = basePattern.addPathSegmentVariables()
        assertThat(result.url.toString()).isEqualTo("https://example.com/")
    }

    // ==================== addQueryParameterVariables Tests ====================

    @Test
    fun `addQueryParameterVariables with single parameter appends correctly`() {
        val result = basePattern.addQueryParameterVariables("filter")
        val resultUrl = result.url.toString()
        assertThat(resultUrl.startsWith("https://example.com?")).isTrue()
        assertThat(resultUrl.contains("filter")).isTrue()
    }

    @Test
    fun `addQueryParameterVariables with multiple parameters appends correctly`() {
        val result = basePattern.addQueryParameterVariables("filter", "sort", "page")
        val resultUrl = result.url.toString()
        assertThat(resultUrl.startsWith("https://example.com?")).isTrue()
        assertThat(resultUrl.contains("filter")).isTrue()
        assertThat(resultUrl.contains("sort")).isTrue()
        assertThat(resultUrl.contains("page")).isTrue()
    }

    @Test
    fun `addQueryParameterVariables on pattern with existing path appends correctly`() {
        val patternWithPath = DeepLinkPattern(Url("https://example.com/users"))
        val result = patternWithPath.addQueryParameterVariables("userId")
        val resultUrl = result.url.toString()
        assertThat(resultUrl.startsWith("https://example.com/users?")).isTrue()
        assertThat(resultUrl.contains("userId")).isTrue()
    }

    @Test
    fun `addQueryParameterVariables returns new DeepLinkPattern instance`() {
        val result = basePattern.addQueryParameterVariables("test")
        assertThat(result === basePattern).isFalse()
    }

    @Test
    fun `addQueryParameterVariables with empty varargs appends only question mark`() {
        val result = basePattern.addQueryParameterVariables()
        assertThat(result.url.toString()).isEqualTo("https://example.com?")
    }

    // ==================== Chaining Tests ====================

    @Test
    fun `addPathSegments and addPathSegmentVariables can be chained`() {
        val result = basePattern
            .addPathSegments("api", "v1")
            .addPathSegmentVariables("userId")
        val expectedUrl = "https://example.com/api/v1/{userId}"
        assertThat(result.url.toString()).isEqualTo(expectedUrl)
    }

    @Test
    fun `addPathSegments chained multiple times appends correctly`() {
        val result = basePattern
            .addPathSegments("api")
            .addPathSegments("v1")
            .addPathSegments("users")
        val expectedUrl = "https://example.com/api/v1/users"
        assertThat(result.url.toString()).isEqualTo(expectedUrl)
    }

    @Test
    fun `all three extension methods can be chained together`() {
        val result = basePattern
            .addPathSegments("api", "v1")
            .addPathSegmentVariables("userId")
            .addQueryParameterVariables("filter", "sort")
        val resultUrl = result.url.toString()
        assertThat(resultUrl.startsWith("https://example.com/api/v1/{userId}?")).isTrue()
        assertThat(resultUrl.contains("filter")).isTrue()
        assertThat(resultUrl.contains("sort")).isTrue()
    }

    // ==================== Edge Cases ====================

    @Test
    fun `addPathSegments with special characters in segments`() {
        val result = basePattern.addPathSegments("api-v1", "user_profile")
        assertThat(result.url.toString()).isEqualTo("https://example.com/api-v1/user_profile")
    }

    @Test
    fun `addPathSegments with numeric segments`() {
        val result = basePattern.addPathSegments("123", "456")
        assertThat(result.url.toString()).isEqualTo("https://example.com/123/456")
    }

    @Test
    fun `basePattern with trailing slash behavior`() {
        val patternWithTrailingSlash = DeepLinkPattern(Url("https://example.com/"))
        val result = patternWithTrailingSlash.addPathSegments("users")
        assertThat(result.url.toString()).isEqualTo("https://example.com//users")
    }
}
