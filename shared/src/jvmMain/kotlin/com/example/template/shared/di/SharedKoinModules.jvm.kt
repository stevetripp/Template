package com.example.template.shared.di

import com.example.template.shared.model.webservice.googleBooksSetup
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import org.koin.core.module.Module
import org.koin.dsl.module

actual val httpClientModule: Module = module {
    // Google Books API HttpClient
    single<HttpClient> { HttpClient(Java) { googleBooksSetup() } }
}
