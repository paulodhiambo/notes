package io.notes.notes.feature_notes.domain.use_case

import io.notes.notes.feature_notes.domain.model.InvalidNoteException
import io.notes.notes.feature_notes.domain.model.Note
import io.notes.notes.feature_notes.domain.repository.NoteRepository

class SaveNoteUseCase(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("Note title cannot be empty")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("Note content cannot be empty")
        }
        repository.insertNote(note)
    }
}