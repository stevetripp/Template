package com.example.template.shared.model.db.note

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.template.shared.model.domain.Note
import kotlin.time.Instant

@Entity(tableName = "Note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val isDone: Boolean = false,
    val created: Instant,
) {
    fun toDomain(): Note = Note(id = id, text = text, isDone = isDone, created = created)
}
