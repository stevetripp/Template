package com.example.template.model.webservice

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.resources.Resource
import org.lds.mobile.ext.executeSafely
import org.lds.mobile.network.ApiResponse
import io.ktor.client.plugins.resources.get as getResource

class GoogleBooksService(
    private val httpClient: HttpClient
) {
    suspend fun getVolumes(q: String, maxResults: Int): ApiResponse<out VolumesDto, out Unit> {
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

