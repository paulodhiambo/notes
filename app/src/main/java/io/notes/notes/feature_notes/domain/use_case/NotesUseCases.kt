package io.notes.notes.feature_notes.domain.use_case

data class NotesUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase,
    val saveNoteUseCase: SaveNoteUseCase
)
