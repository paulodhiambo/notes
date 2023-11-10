package io.notes.notes.feature_notes.presentation.notes.state

import io.notes.notes.feature_notes.domain.model.Note
import io.notes.notes.feature_notes.domain.util.NoteOrder
import io.notes.notes.feature_notes.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
