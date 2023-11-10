package io.notes.notes.feature_notes.presentation.notes.events

import io.notes.notes.feature_notes.domain.model.Note
import io.notes.notes.feature_notes.domain.util.NoteOrder

sealed class NoteEvent {
    data class Order(val order: NoteOrder) : NoteEvent()
    data class DeleteNote(val note: Note) : NoteEvent()
    data object RestoreNote : NoteEvent()
    data object ToggleOrderSection : NoteEvent()
}