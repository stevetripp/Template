package com.example.template.model.webservice

import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.LoggingConfig
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Project Specific Defaults (should not be in shared library)
 */
object KtorClientDefaults {
    fun LoggingConfig.defaultSetup() {
        logger = KermitKtorLogger
        level = LogLevel.INFO
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }

    fun ContentNegotiationConfig.defaultSetup(
        allowAnyContentType: Boolean = false,
        jsonPrettyPrint: Boolean = false
    ) {
        val jsonDefinition = Json {
            prettyPrint = jsonPrettyPrint
            isLenient = true
            coerceInputValues = true // incorrect JSON values to the default
            ignoreUnknownKeys = true
        }

        json(jsonDefinition)

        // work-around for github that does NOT return Content-Type: application/json (github returns  Content-Type: text/plain; charset=utf-8)
        if (allowAnyContentType) {
            register(ContentType.Any, KotlinxSerializationConverter(jsonDefinition))
        }
    }

    fun DefaultRequest.DefaultRequestBuilder.setupStandardHeaders() {
        accept(ContentType.Application.Json)
        contentType(ContentType.Application.Json)
//        userAgent(UserAgentUtil.createUserAgent(BuildConfig.USER_AGENT_APP_NAME, BuildConfig.VERSION_NAME))
//
//        headers.append("client-app-name", "gLib-Android") // used by verse of the day service
//        headers.append("client-app-version", BuildConfig.VERSION_NAME) // used by annotation team to identify version source
    }
}

private object KermitKtorLogger : Logger {
    override fun log(message: String) {
        co.touchlab.kermit.Logger.i { message }
    }
}
