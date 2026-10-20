package com.example.template.shared.di

import com.example.template.shared.model.webservice.googleBooksSetup
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

actual val httpClientModule = module {
    // Google Books API HttpClient
    single<HttpClient> { HttpClient(OkHttp.create()) { googleBooksSetup() } }
}
