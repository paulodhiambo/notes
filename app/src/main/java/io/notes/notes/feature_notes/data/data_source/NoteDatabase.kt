package io.notes.notes.feature_notes.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import io.notes.notes.feature_notes.domain.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNotesDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "notes.db"
    }
}