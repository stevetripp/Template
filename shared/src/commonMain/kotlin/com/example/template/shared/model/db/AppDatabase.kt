package com.example.template.shared.model.db

import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.template.shared.model.db.converter.InstantConverter
import com.example.template.shared.model.db.note.NoteDao
import com.example.template.shared.model.db.note.NoteEntity

@Database(
    entities = [
        NoteEntity::class,
    ],
    version = 1,
)
@ConstructedBy(AppDatabaseConstructor::class)
@ColumnTypeConverters(
    InstantConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "main.db"

        fun getDatabase(builder: Builder<AppDatabase>): AppDatabase {
            return builder
                .fallbackToDestructiveMigration(true)
                .setDriver(BundledSQLiteDriver())
                .build()
        }
    }
}

// Room generates the actual implementations for each platform
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
