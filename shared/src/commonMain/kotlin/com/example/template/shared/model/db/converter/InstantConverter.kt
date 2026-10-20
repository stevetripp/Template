package com.example.template.shared.model.db.converter

import androidx.room3.ColumnTypeConverter
import kotlin.time.Instant

object InstantConverter {
    @ColumnTypeConverter
    fun fromInstantToLong(value: Instant): Long = value.toEpochMilliseconds()

    @ColumnTypeConverter
    fun fromLongToInstant(value: Long): Instant = Instant.fromEpochMilliseconds(value)
}
