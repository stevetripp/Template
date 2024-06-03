package com.example.template.ux.ktor

data class KtorUiState(
    val onExecute: () -> Unit = {}
)