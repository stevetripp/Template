@file:Suppress("unused")

package org.lds.mobile.ext

import android.app.Application
import android.os.Build
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

fun DefaultRequest.DefaultRequestBuilder.acceptLanguage(application: Application) {
    val locales = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        application.resources.configuration.locales.toLanguageTags()
    } else {
        @Suppress("DEPRECATION")
        application.resources.configuration.locale.toLanguageTag()
    }
    header(HttpHeaders.AcceptLanguage, locales)
}
