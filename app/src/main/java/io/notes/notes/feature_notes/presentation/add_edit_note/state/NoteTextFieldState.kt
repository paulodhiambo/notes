package io.notes.notes.feature_notes.presentation.add_edit_note.state

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)