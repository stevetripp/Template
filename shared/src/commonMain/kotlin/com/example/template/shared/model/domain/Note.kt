package com.example.template.shared.model.domain

import kotlin.time.Instant

data class Note(
    val id: Long,
    val text: String,
    val isDone: Boolean,
    val created: Instant,
)
