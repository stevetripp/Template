package com.example.template.model.webservice

import com.example.template.model.webservice.KtorClientDefaults.defaultSetup
import com.example.template.model.webservice.KtorClientDefaults.setupStandardHeaders
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingConfig
import io.ktor.client.plugins.resources.Resources
import io.ktor.resources.Resource
import org.lds.mobile.ext.executeSafely
import org.lds.mobile.network.ApiResponse
import io.ktor.client.plugins.resources.get as getResource

class GoogleBooksService(
    engine: HttpClientEngine = OkHttp.create(),
    loggingSetup: LoggingConfig.() -> Unit = { defaultSetup() },
    contentNegotiationSetup: ContentNegotiationConfig.() -> Unit = { defaultSetup(allowAnyContentType = true) },
) {
    private val httpClient: HttpClient = HttpClient(engine) {
        install(Logging) { loggingSetup() }
        install(Resources)
        install(ContentNegotiation) { contentNegotiationSetup() }
        defaultRequest {
            url("https://www.googleapis.com/books/")
            setupStandardHeaders()
        }
    }.apply {
//        installOktaAuthPlugin(authenticationManager, useUserToken = false)
    }

    suspend fun getVolumes(q: String, maxResults: Int): ApiResponse<VolumesDto, Unit> {
        return httpClient.executeSafely({ getResource(GoogleBooksV1ServiceResource.Volumes(q = q, maxResults = maxResults)) }) { it.body() }
    }
}

@Resource("/v1")
private object GoogleBooksV1ServiceResource {
    @Resource("/volumes")
    class Volumes(
        val parent: GoogleBooksV1ServiceResource = GoogleBooksV1ServiceResource,
        val q: String,
        val maxResults: Int,
//        val device: String = "android",
//        val version: String = "0.0.1"
    )
}

