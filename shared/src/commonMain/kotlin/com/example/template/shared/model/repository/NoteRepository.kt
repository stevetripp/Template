package com.example.template.shared.model.repository

import com.example.template.shared.model.db.AppDatabase
import com.example.template.shared.model.db.note.NoteEntity
import com.example.template.shared.model.domain.Note
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.Flow
import org.lds.mobile.ext.mapDistinct

/**
 * Business-logic layer over the [AppDatabase] Note table. Screens observe [notesFlow]; the table is the single source of truth.
 */
interface NoteRepository {
    val notesFlow: Flow<List<Note>>

    /** Saves [text] trimmed; blank text is ignored. Returns the new note's id, or null if nothing was saved. */
    suspend fun addNote(text: String): Long?

    /** Inserts [SAMPLE_NOTES] (adding to any existing notes); the first note in the list is the newest. */
    suspend fun seedNotes()
    suspend fun setDone(id: Long, isDone: Boolean)
    suspend fun delete(id: Long)
    suspend fun deleteDone()
    suspend fun deleteAll()

    companion object {
        /** Sample note text paired with whether it is done. */
        val SAMPLE_NOTES: List<Pair<String, Boolean>> = listOf(
            "Buy milk" to false,
            "Walk the dog" to false,
            "Call the dentist" to false,
            "Read a chapter of a book" to false,
            "Water the plants" to true,
            "Pay the electric bill" to true,
        )
    }
}

class NoteRepositoryImpl(
    appDatabase: AppDatabase,
    private val clock: Clock = Clock.System,
) : NoteRepository {
    private val noteDao = appDatabase.noteDao()

    override val notesFlow: Flow<List<Note>> = noteDao.getAllFlow().mapDistinct { entities -> entities.map { it.toDomain() } }

    override suspend fun addNote(text: String): Long? {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return null
        return noteDao.insert(NoteEntity(text = trimmed, created = clock.now()))
    }

    override suspend fun seedNotes() {
        val now = clock.now()
        // Stagger created times so the list keeps SAMPLE_NOTES order (newest first)
        val entities = NoteRepository.SAMPLE_NOTES.mapIndexed { index, (text, isDone) ->
            NoteEntity(text = text, isDone = isDone, created = now - index.milliseconds)
        }
        noteDao.insertAll(entities)
    }

    override suspend fun setDone(id: Long, isDone: Boolean) = noteDao.updateDone(id, isDone)
    override suspend fun delete(id: Long) = noteDao.delete(id)
    override suspend fun deleteDone() = noteDao.deleteDone()
    override suspend fun deleteAll() = noteDao.deleteAll()
}
