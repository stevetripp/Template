package com.example.template.shared.model.repository

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.template.shared.model.db.AppDatabase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Runs against a real in-memory Room database.
 */
class NoteRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: NoteRepository
    private var now = Instant.fromEpochMilliseconds(1_000_000)

    @BeforeTest
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder<AppDatabase>()
            .setDriver(BundledSQLiteDriver())
            .build()
        val clock = object : Clock {
            override fun now(): Instant = now.also { now += 1.seconds }
        }
        repository = NoteRepositoryImpl(database, clock)
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun `an added note is saved trimmed`() = runBlocking {
        repository.addNote("  Buy milk ")

        assertEquals(listOf("Buy milk"), repository.notesFlow.first().map { it.text })
    }

    @Test
    fun `blank text is not saved`() = runBlocking {
        assertNull(repository.addNote("   "))

        assertTrue(repository.notesFlow.first().isEmpty())
    }

    @Test
    fun `open notes come first, newest first`() = runBlocking {
        val first = checkNotNull(repository.addNote("first"))
        repository.addNote("second")
        repository.addNote("third")

        repository.setDone(first, true)

        assertEquals(listOf("third", "second", "first"), repository.notesFlow.first().map { it.text })
        assertTrue(repository.notesFlow.first().last().isDone)
    }

    @Test
    fun `seeding inserts the sample notes alongside existing notes`() = runBlocking {
        repository.addNote("existing")

        repository.seedNotes()

        val notes = repository.notesFlow.first()
        val openTexts = NoteRepository.SAMPLE_NOTES.filterNot { it.second }.map { it.first }
        val doneTexts = NoteRepository.SAMPLE_NOTES.filter { it.second }.map { it.first }
        assertEquals(NoteRepository.SAMPLE_NOTES.size + 1, notes.size)
        assertEquals(openTexts + "existing", notes.filterNot { it.isDone }.map { it.text })
        assertEquals(doneTexts, notes.filter { it.isDone }.map { it.text })
    }

    @Test
    fun `deleteDone removes only completed notes`() = runBlocking {
        val done = checkNotNull(repository.addNote("done"))
        repository.addNote("open")
        repository.setDone(done, true)

        repository.deleteDone()

        assertEquals(listOf("open"), repository.notesFlow.first().map { it.text })
    }

    @Test
    fun `delete and deleteAll remove notes`() = runBlocking {
        val id = checkNotNull(repository.addNote("one"))
        repository.addNote("two")

        repository.delete(id)
        assertEquals(listOf("two"), repository.notesFlow.first().map { it.text })

        repository.deleteAll()
        assertTrue(repository.notesFlow.first().isEmpty())
    }
}
