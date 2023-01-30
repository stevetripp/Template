package com.example.template.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.stateInDefault(coroutineScope: CoroutineScope, initialValue: T): StateFlow<T> = this.stateIn(
    scope = coroutineScope,
    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
    initialValue = initialValue,
)