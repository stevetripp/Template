package com.example.template.util

import com.example.template.ux.DeepLink
import com.example.template.ux.parameters.DestinationRoute
import com.example.template.ux.parameters.EnumParameter
import com.example.template.ux.parameters.Parameter1
import com.example.template.ux.parameters.deepLinkUrl
import io.ktor.http.Url
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class DeepLinkUrlTest {

    @Test
    fun testDeepLinkUrl() {
        // Test exact path match without dynamic segments
        val deepLink1 = DeepLinkUrl("https://example.com/users")
        assertEquals(true, deepLink1.matches(Url("https://example.com/users")))
        assertEquals(false, deepLink1.matches(Url("https://example.com/posts")))

        // Test path match with one dynamic segment
        val deepLink2 = DeepLinkUrl("https://example.com/users/123")
        assertEquals(true, deepLink2.matches(Url("https://example.com/users/{userId}")))
        assertEquals(false, deepLink2.matches(Url("https://example.com/posts/{postId}")))

        // Test path match with multiple dynamic segments
        val deepLink3 = DeepLinkUrl("https://example.com/users/123/posts/456")
        assertEquals(true, deepLink3.matches(Url("https://example.com/users/{userId}/posts/{postId}")))
        assertEquals(false, deepLink3.matches(Url("https://example.com/users/{userId}/comments/{commentId}")))

        // Test path length mismatch
        val deepLink4 = DeepLinkUrl("https://example.com/users/123")
        assertEquals(false, deepLink4.matches(Url("https://example.com/users/123/posts/456")))

        // Test encoded characters in path
        val deepLink5 = DeepLinkUrl("https://example.com/search/kotlin%20rocks")
        assertEquals(true, deepLink5.matches(Url("https://example.com/search/{query}")))

        // Test query string parameters matching
        val deepLink6 = DeepLinkUrl("https://example.com/users/123?role=admin&status=active")
        assertEquals(true, deepLink6.matches(Url("https://example.com/users/{userId}")))
        assertEquals(false, deepLink6.matches(Url("https://example.com/posts/{postId}")))

        // Test path and query parameters together
        val deepLink7 = DeepLinkUrl("https://example.com/users/456/posts/789?sort=desc&filter=recent")
        assertEquals(true, deepLink7.matches(Url("https://example.com/users/{userId}/posts/{postId}")))
    }

    @Test
    fun testGetParameters() {
        // Test: No dynamic segments, no query parameters
        val deepLink1 = DeepLinkUrl("https://example.com/users")
        val params1 = deepLink1.getParameters(Url("https://example.com/users"))
        assertEquals(emptyMap<String, String>(), params1)

        // Test: One dynamic path segment, no query parameters
        val deepLink2 = DeepLinkUrl("https://example.com/users/123")
        val params2 = deepLink2.getParameters(Url("https://example.com/users/{userId}"))
        assertEquals(mapOf("userId" to "123"), params2)

        // Test: One dynamic path segment with one query parameter
        val deepLink3 = DeepLinkUrl("https://example.com/users/123?role=admin")
        val params3 = deepLink3.getParameters(Url("https://example.com/users/{userId}"))
        assertEquals(mapOf("userId" to "123", "role" to "admin"), params3)

        // Test: Multiple dynamic path segments with multiple query parameters
        val deepLink4 = DeepLinkUrl("https://example.com/users/456/posts/789?sort=desc&filter=recent")
        val params4 = deepLink4.getParameters(Url("https://example.com/users/{userId}/posts/{postId}"))
        assertEquals(
            mapOf("userId" to "456", "postId" to "789", "sort" to "desc", "filter" to "recent"),
            params4
        )

        // Test: Encoded characters in path segment
        val deepLink5 = DeepLinkUrl("https://example.com/search/kotlin%20rocks")
        val params5 = deepLink5.getParameters(Url("https://example.com/search/{query}"))
        assertEquals(mapOf("query" to "kotlin rocks"), params5)

        // Test: Encoded characters in path with query parameters
        val deepLink6 = DeepLinkUrl("https://example.com/search/hello%20world?lang=en&limit=10")
        val params6 = deepLink6.getParameters(Url("https://example.com/search/{query}"))
        assertEquals(mapOf("query" to "hello world", "lang" to "en", "limit" to "10"), params6)

        // Test: Path length mismatch returns empty map (no matching parameters extracted)
        val deepLink7 = DeepLinkUrl("https://example.com/users/123")
        val params7 = deepLink7.getParameters(Url("https://example.com/users/123/posts/456"))
        assertEquals(emptyMap<String, String>(), params7)

        // Test: Multiple query parameters without path parameters
        val deepLink8 = DeepLinkUrl("https://example.com/users?role=admin&status=active&verified=true")
        val params8 = deepLink8.getParameters(Url("https://example.com/users"))
        assertEquals(mapOf("role" to "admin", "status" to "active", "verified" to "true"), params8)
    }

    @Test
    fun testToRoute() {
        // Test: Convert URL to DestinationRoute with required path parameters and required query parameters
        val patternUrl = DestinationRoute.deepLinkUrl
        val deepLink1 = DeepLinkUrl("${DeepLink.ROOT}/DESTINATION/value1/ONE?queryParam1=query1&queryParam2=TWO")
        val route1 = deepLink1.toRoute<DestinationRoute>(patternUrl)

        assertEquals(Parameter1("value1"), route1.reqParam1)
        assertEquals(EnumParameter.ONE, route1.reqParam2)
        assertEquals(Parameter1("query1"), route1.optParam1)
        assertEquals(EnumParameter.TWO, route1.optParam2)

        // Test: Convert URL to DestinationRoute with only required path parameters
        val deepLink2 = DeepLinkUrl("${DeepLink.ROOT}/DESTINATION/value2/THREE")
        val route2 = deepLink2.toRoute<DestinationRoute>(patternUrl)

        assertEquals(Parameter1("value2"), route2.reqParam1)
        assertEquals(EnumParameter.THREE, route2.reqParam2)
        assertNull(route2.optParam1)
        assertNull(route2.optParam2)

        // Test: Convert URL to DestinationRoute with only one optional query parameter
        val deepLink3 = DeepLinkUrl("${DeepLink.ROOT}/DESTINATION/value3/TWO?queryParam1=query3")
        val route3 = deepLink3.toRoute<DestinationRoute>(patternUrl)

        assertEquals(Parameter1("value3"), route3.reqParam1)
        assertEquals(EnumParameter.TWO, route3.reqParam2)
        assertEquals(Parameter1("query3"), route3.optParam1)
        assertNull(route3.optParam2)

        // Test: Convert URL with encoded characters in path parameter
        val deepLink4 = DeepLinkUrl("${DeepLink.ROOT}/DESTINATION/test%20value/ONE")
        val route4 = deepLink4.toRoute<DestinationRoute>(patternUrl)

        assertEquals(Parameter1("test value"), route4.reqParam1)
        assertEquals(EnumParameter.ONE, route4.reqParam2)

        // Test: Convert URL with optional Int parameter (optParam3)
        val deepLink5 = DeepLinkUrl("${DeepLink.ROOT}/DESTINATION/value5/ONE?queryParam1=query5&queryParam3=42")
        val route5 = deepLink5.toRoute<DestinationRoute>(patternUrl)

        assertEquals(Parameter1("value5"), route5.reqParam1)
        assertEquals(EnumParameter.ONE, route5.reqParam2)
        assertEquals(Parameter1("query5"), route5.optParam1)
        assertEquals(42, route5.optParam3)
    }
}