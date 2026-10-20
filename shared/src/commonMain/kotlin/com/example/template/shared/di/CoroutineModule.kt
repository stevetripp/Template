package com.example.template.shared.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

// ===== Coroutine Module =====
interface CoroutineDispatchers {
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}

val coroutineModule = module {
    // ===== Scopes =====
    // ApplicationScope (No name on this one so that it IS the default one that is used if there is no name is provided)
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // ===== Dispatchers =====
    // IoDispatcher (No name on this one so that it IS the default one that is used if there is no name is provided)
    single<CoroutineDispatchers> {
        object : CoroutineDispatchers {
            override val default = Dispatchers.Default
            override val io = Dispatchers.IO
            override val main = Dispatchers.Main
        }
    }
}
