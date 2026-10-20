package com.example.template.shared.model.webservice

import com.example.template.shared.model.webservice.KtorClientDefaults.defaultSetup
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.lds.mobile.network.ApiResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GoogleBooksServiceTest {

    @Test
    fun `volumes request is built from the resource and parsed`() = runTest {
        var requestedUrl: String? = null
        val service = serviceFor { url ->
            requestedUrl = url
            respond(VOLUMES_JSON, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }

        val response = service.getVolumes(q = "kotlin", maxResults = 5)

        assertEquals("https://www.googleapis.com/books/v1/volumes?q=kotlin&maxResults=5", requestedUrl)
        val success = assertIs<ApiResponse.Success<VolumesDto>>(response)
        assertEquals(0, success.data.totalItems)
    }

    @Test
    fun `server errors map to Failure Error Server`() = runTest {
        val service = serviceFor { respond("", HttpStatusCode.InternalServerError) }

        assertIs<ApiResponse.Failure.Error.Server>(service.getVolumes(q = "kotlin", maxResults = 5))
    }

    private fun serviceFor(handler: suspend io.ktor.client.engine.mock.MockRequestHandleScope.(String) -> io.ktor.client.request.HttpResponseData): GoogleBooksService {
        val engine = MockEngine { request -> handler(request.url.toString()) }
        val client = HttpClient(engine) {
            install(Resources)
            install(ContentNegotiation) { defaultSetup(allowAnyContentType = true) }
            defaultRequest { url("https://www.googleapis.com/books/") }
        }
        return GoogleBooksService(client)
    }

    private companion object {
        const val VOLUMES_JSON = """{"totalItems":0,"kind":"books#volumes","items":[]}"""
    }
}
