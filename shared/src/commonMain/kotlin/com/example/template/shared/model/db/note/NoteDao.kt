package com.example.template.shared.model.db.note

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Open notes first, then newest first
    @Query("SELECT * FROM Note ORDER BY isDone, created DESC, id DESC")
    fun getAllFlow(): Flow<List<NoteEntity>>

    @Insert
    suspend fun insert(entity: NoteEntity): Long

    @Insert
    suspend fun insertAll(entities: List<NoteEntity>)

    @Query("UPDATE Note SET isDone = :isDone WHERE id = :id")
    suspend fun updateDone(id: Long, isDone: Boolean)

    @Query("DELETE FROM Note WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM Note WHERE isDone = 1")
    suspend fun deleteDone()

    @Query("DELETE FROM Note")
    suspend fun deleteAll()
}
