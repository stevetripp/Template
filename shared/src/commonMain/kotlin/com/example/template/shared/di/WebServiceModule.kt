package com.example.template.shared.di

import com.example.template.shared.model.webservice.GoogleBooksService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

// ===== Service Module =====
val webServiceModule = module {
    // Google Books Service
    singleOf(::GoogleBooksService)
}
