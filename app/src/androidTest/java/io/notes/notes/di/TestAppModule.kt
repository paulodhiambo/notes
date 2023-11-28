package io.notes.notes.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.notes.notes.feature_notes.data.data_source.NoteDatabase
import io.notes.notes.feature_notes.data.repository.NoteRepositoryImpl
import io.notes.notes.feature_notes.domain.repository.NoteRepository
import io.notes.notes.feature_notes.domain.use_case.DeleteNoteUseCase
import io.notes.notes.feature_notes.domain.use_case.GetNoteByIdUseCase
import io.notes.notes.feature_notes.domain.use_case.GetNotesUseCase
import io.notes.notes.feature_notes.domain.use_case.NotesUseCases
import io.notes.notes.feature_notes.domain.use_case.SaveNoteUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application): NoteDatabase {
        return Room.inMemoryDatabaseBuilder(app, NoteDatabase::class.java)
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.getNotesDao())
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases {
        return NotesUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            getNoteByIdUseCase = GetNoteByIdUseCase(repository),
            saveNoteUseCase = SaveNoteUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository)
        )
    }
}
