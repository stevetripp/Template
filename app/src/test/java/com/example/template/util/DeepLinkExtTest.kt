package com.example.template.util

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.example.template.ux.parameters.DestinationRoute
import com.example.template.ux.parameters.EnumParameter
import com.example.template.ux.parameters.Parameter1
import com.example.template.ux.parameters.deepLinkPattern
import io.ktor.http.Url
import org.junit.jupiter.api.Test
import org.lds.mobile.navigation3.DeepLink
import org.lds.mobile.navigation3.DeepLinkPattern

class DeepLinkExtTest {

    @Test
    fun testDeepLinkUrl() {
        // Test exact path match without dynamic segments
        val deepLink1 = DeepLink(Url("https://example.com/users"))
        assertThat(deepLink1.matches(DeepLinkPattern(Url("https://example.com/users")))).isTrue()
        assertThat(deepLink1.matches(DeepLinkPattern(Url("https://example.com/posts")))).isFalse()

        // Test path match with one dynamic segment
        val deepLink2 = DeepLink(Url("https://example.com/users/123"))
        assertThat(deepLink2.matches(DeepLinkPattern(Url("https://example.com/users/{userId}")))).isTrue()
        assertThat(deepLink2.matches(DeepLinkPattern(Url("https://example.com/posts/{postId}")))).isFalse()

        // Test path match with multiple dynamic segments
        val deepLink3 = DeepLink(Url("https://example.com/users/123/posts/456"))
        assertThat(deepLink3.matches(DeepLinkPattern(Url("https://example.com/users/{userId}/posts/{postId}")))).isTrue()
        assertThat(deepLink3.matches(DeepLinkPattern(Url("https://example.com/users/{userId}/comments/{commentId}")))).isFalse()

        // Test path length mismatch
        val deepLink4 = DeepLink(Url("https://example.com/users/123"))
        assertThat(deepLink4.matches(DeepLinkPattern(Url("https://example.com/users/123/posts/456")))).isFalse()

        // Test encoded characters in path
        val deepLink5 = DeepLink(Url("https://example.com/search/kotlin%20rocks"))
        assertThat(deepLink5.matches(DeepLinkPattern(Url("https://example.com/search/{query}")))).isTrue()

        // Test query string parameters matching
        val deepLink6 = DeepLink(Url("https://example.com/users/123?role=admin&status=active"))
        assertThat(deepLink6.matches(DeepLinkPattern(Url("https://example.com/users/{userId}")))).isTrue()
        assertThat(deepLink6.matches(DeepLinkPattern(Url("https://example.com/posts/{postId}")))).isFalse()

        // Test path and query parameters together
        val deepLink7 = DeepLink(Url("https://example.com/users/456/posts/789?sort=desc&filter=recent"))
        assertThat(deepLink7.matches(DeepLinkPattern(Url("https://example.com/users/{userId}/posts/{postId}")))).isTrue()

        // Test invalid protocol - should not match
        val deepLink8 = DeepLink(Url("invalid://example.com/users"))
        assertThat(deepLink8.matches(DeepLinkPattern(Url("https://example.com/users")))).isEqualTo(false)
        assertThat(deepLink8.matches(DeepLinkPattern(Url("invalid://example.com/users")))).isEqualTo(true)
    }

    @Test
    fun testGetParameters() {
        // Test: No dynamic segments, no query parameters
        val deepLink1 = DeepLink(Url("https://example.com/users"))
        val params1 = deepLink1.getParameters(DeepLinkPattern(Url("https://example.com/users")))
        assertThat(params1).isEmpty()

        // Test: One dynamic path segment, no query parameters
        val deepLink2 = DeepLink(Url("https://example.com/users/123"))
        val params2 = deepLink2.getParameters(DeepLinkPattern(Url("https://example.com/users/{userId}")))
        assertThat(params2).isEqualTo(mapOf("userId" to "123"))

        // Test: One dynamic path segment with one query parameter
        val deepLink3 = DeepLink(Url("https://example.com/users/123?role=admin"))
        val params3 = deepLink3.getParameters(DeepLinkPattern(Url("https://example.com/users/{userId}")))
        assertThat(params3).isEqualTo(mapOf("userId" to "123", "role" to "admin"))

        // Test: Multiple dynamic path segments with multiple query parameters
        val deepLink4 = DeepLink(Url("https://example.com/users/456/posts/789?sort=desc&filter=recent"))
        val params4 = deepLink4.getParameters(DeepLinkPattern(Url("https://example.com/users/{userId}/posts/{postId}")))
        assertThat(params4).isEqualTo(
            mapOf("userId" to "456", "postId" to "789", "sort" to "desc", "filter" to "recent")
        )

        // Test: Encoded characters in path segment
        val deepLink5 = DeepLink(Url("https://example.com/search/kotlin%20rocks"))
        val params5 = deepLink5.getParameters(DeepLinkPattern(Url("https://example.com/search/{query}")))
        assertThat(params5).isEqualTo(mapOf("query" to "kotlin rocks"))

        // Test: Encoded characters in path with query parameters
        val deepLink6 = DeepLink(Url("https://example.com/search/hello%20world?lang=en&limit=10"))
        val params6 = deepLink6.getParameters(DeepLinkPattern(Url("https://example.com/search/{query}")))
        assertThat(params6).isEqualTo(mapOf("query" to "hello world", "lang" to "en", "limit" to "10"))

        // Test: Path length mismatch returns empty map (no matching parameters extracted)
        val deepLink7 = DeepLink(Url("https://example.com/users/123"))
        val params7 = deepLink7.getParameters(DeepLinkPattern(Url("https://example.com/users/123/posts/456")))
        assertThat(params7).isEmpty()

        // Test: Multiple query parameters without path parameters
        val deepLink8 = DeepLink(Url("https://example.com/users?role=admin&status=active&verified=true"))
        val params8 = deepLink8.getParameters(DeepLinkPattern(Url("https://example.com/users")))
        assertThat(params8).isEqualTo(mapOf("role" to "admin", "status" to "active", "verified" to "true"))
    }

    @Test
    fun testToRoute() {
        // Test: Convert URL to DestinationRoute with required path parameters and required query parameters
        val patternUrl = DestinationRoute.deepLinkPattern
        val deepLink1 = DeepLink(Url("${AppDeepLinks.ROOT}/DESTINATION/value1/ONE?queryParam1=query1&queryParam2=TWO"))
        val route1 = deepLink1.toRoute<DestinationRoute>(patternUrl)

        assertThat(route1.reqParam1).isEqualTo(Parameter1("value1"))
        assertThat(route1.reqParam2).isEqualTo(EnumParameter.ONE)
        assertThat(route1.optParam1).isEqualTo(Parameter1("query1"))
        assertThat(route1.optParam2).isEqualTo(EnumParameter.TWO)

        // Test: Convert URL to DestinationRoute with only required path parameters
        val deepLink2 = DeepLink(Url("${AppDeepLinks.ROOT}/DESTINATION/value2/THREE"))
        val route2 = deepLink2.toRoute<DestinationRoute>(patternUrl)

        assertThat(route2.reqParam1).isEqualTo(Parameter1("value2"))
        assertThat(route2.reqParam2).isEqualTo(EnumParameter.THREE)
        assertThat(route2.optParam1).isNull()
        assertThat(route2.optParam2).isNull()

        // Test: Convert URL to DestinationRoute with only one optional query parameter
        val deepLink3 = DeepLink(Url("${AppDeepLinks.ROOT}/DESTINATION/value3/TWO?queryParam1=query3"))
        val route3 = deepLink3.toRoute<DestinationRoute>(patternUrl)

        assertThat(route3.reqParam1).isEqualTo(Parameter1("value3"))
        assertThat(route3.reqParam2).isEqualTo(EnumParameter.TWO)
        assertThat(route3.optParam1).isEqualTo(Parameter1("query3"))
        assertThat(route3.optParam2).isNull()

        // Test: Convert URL with encoded characters in path parameter
        val deepLink4 = DeepLink(Url("${AppDeepLinks.ROOT}/DESTINATION/test%20value/ONE"))
        val route4 = deepLink4.toRoute<DestinationRoute>(patternUrl)

        assertThat(route4.reqParam1).isEqualTo(Parameter1("test value"))
        assertThat(route4.reqParam2).isEqualTo(EnumParameter.ONE)

        // Test: Convert URL with optional Int parameter (optParam3)
        val deepLink5 = DeepLink(Url("${AppDeepLinks.ROOT}/DESTINATION/value5/ONE?queryParam1=query5&queryParam3=42"))
        val route5 = deepLink5.toRoute<DestinationRoute>(patternUrl)

        assertThat(route5.reqParam1).isEqualTo(Parameter1("value5"))
        assertThat(route5.reqParam2).isEqualTo(EnumParameter.ONE)
        assertThat(route5.optParam1).isEqualTo(Parameter1("query5"))
        assertThat(route5.optParam3).isEqualTo(42)
    }
}