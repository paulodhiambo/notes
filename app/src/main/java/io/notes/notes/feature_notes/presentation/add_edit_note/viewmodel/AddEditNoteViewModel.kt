package io.notes.notes.feature_notes.presentation.add_edit_note.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.notes.notes.feature_notes.domain.model.InvalidNoteException
import io.notes.notes.feature_notes.domain.model.Note
import io.notes.notes.feature_notes.domain.use_case.NotesUseCases
import io.notes.notes.feature_notes.presentation.add_edit_note.event.AddEditNoteEvent
import io.notes.notes.feature_notes.presentation.add_edit_note.state.NoteTextFieldState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _noteTitle = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter title..."
        )
    )
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private var _noteContent = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter some content"
        )
    )
    val noteContent: State<NoteTextFieldState> = _noteContent

    private var _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor


    private var _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null


    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    notesUseCases.getNoteByIdUseCase(noteId)?.also { note ->
                        currentNoteId = note.id

                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )

                        _noteContent.value = noteContent.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )

                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        notesUseCases.saveNoteUseCase(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timeStamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UIEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UIEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = noteColor.value
            }
        }

    }


    sealed class UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent()
        data object SaveNote : UIEvent()
    }
}