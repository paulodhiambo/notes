package io.notes.notes.feature_notes.domain.use_case

import io.notes.notes.feature_notes.domain.model.Note
import io.notes.notes.feature_notes.domain.repository.NoteRepository

class GetNoteByIdUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNote(id)
    }
}