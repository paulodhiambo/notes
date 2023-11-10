package io.notes.notes.feature_notes.domain.use_case

import io.notes.notes.feature_notes.domain.model.Note
import io.notes.notes.feature_notes.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}