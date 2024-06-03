package com.example.template.model.webservice

import com.example.template.model.webservice.KtorClientDefaults.defaultSetup
import com.example.template.model.webservice.KtorClientDefaults.setupStandardHeaders
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Named(GOOGLE_API_STANDARD_CLIENT)
    fun getStandardClient(): HttpClient {
        return HttpClient(OkHttp.create()) {
            install(Logging) { defaultSetup() }
            install(Resources)
            install(ContentNegotiation) { defaultSetup(allowAnyContentType = true) }
            defaultRequest {
                url("https://www.googleapis.com/books/")
                setupStandardHeaders()
            }
        }.apply {
//        installOktaAuthPlugin(authenticationManager, useUserToken = false)
        }
    }

    companion object {
        const val GOOGLE_API_STANDARD_CLIENT = "GOOGLE_API_STANDARD_CLIENT"
    }
}