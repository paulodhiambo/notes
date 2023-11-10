package io.notes.notes.feature_notes.presentation.notes.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.notes.notes.feature_notes.domain.model.Note
import io.notes.notes.feature_notes.domain.use_case.NotesUseCases
import io.notes.notes.feature_notes.domain.util.NoteOrder
import io.notes.notes.feature_notes.domain.util.OrderType
import io.notes.notes.feature_notes.presentation.notes.events.NoteEvent
import io.notes.notes.feature_notes.presentation.notes.state.NotesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases
) : ViewModel() {
    private var _state = mutableStateOf(NotesState())
    private var _recentlyDeleteNote: Note? = null
    val state: State<NotesState> = _state
    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.Order -> {
                if (state.value.noteOrder::class == event.order::class && state.value.noteOrder.orderType == event.order.orderType) {
                    return
                }
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    notesUseCases.deleteNoteUseCase(event.note)
                    _recentlyDeleteNote = event.note
                }
            }

            is NoteEvent.RestoreNote -> {
                viewModelScope.launch {
                    notesUseCases.saveNoteUseCase(_recentlyDeleteNote ?: return@launch)
                    _recentlyDeleteNote = null
                }
            }

            is NoteEvent.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(notesOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = notesUseCases.getNotesUseCase(notesOrder)
            .onEach { notes ->
                _state.value = _state.value.copy(
                    notes = notes,
                    noteOrder = notesOrder
                )
            }.launchIn(viewModelScope)
    }
}