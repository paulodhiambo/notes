package io.notes.notes.feature_notes.domain.use_case

import com.google.common.truth.Truth.assertThat
import io.notes.notes.feature_notes.data.repository.FakeNoteRepository
import io.notes.notes.feature_notes.domain.model.Note
import io.notes.notes.feature_notes.domain.util.NoteOrder
import io.notes.notes.feature_notes.domain.util.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetNotesUseCaseTest {
    private lateinit var getNotesUseCase: GetNotesUseCase
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        getNotesUseCase = GetNotesUseCase(fakeNoteRepository)
        val notesToInsert = mutableListOf<Note>()
        ('a'..'z').forEachIndexed { index, c ->
            notesToInsert.add(
                Note(
                    title = c.toString(),
                    content = c.toString(),
                    timeStamp = index.toLong(),
                    color = index
                )
            )
        }
        notesToInsert.shuffle()
        runBlocking {
            notesToInsert.forEach { note -> fakeNoteRepository.insertNote(note) }
        }
    }

    @Test
    fun `Order notes by title, ascending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Title(OrderType.Ascending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title).isLessThan(notes[i + 1].title)
        }
    }

    @Test
    fun `Order notes by title, descending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Title(OrderType.Descending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i + 1].title)
        }
    }

    @Test
    fun `Order notes by date, ascending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Date(OrderType.Ascending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timeStamp).isLessThan(notes[i + 1].timeStamp)
        }
    }

    @Test
    fun `Order notes by date, descending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Date(OrderType.Descending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timeStamp).isGreaterThan(notes[i + 1].timeStamp)
        }
    }

    @Test
    fun `Order notes by color, ascending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Color(OrderType.Ascending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isLessThan(notes[i + 1].color)
        }
    }

    @Test
    fun `Order notes by color, descending, correct order`() = runBlocking {
        val notes = getNotesUseCase(NoteOrder.Color(OrderType.Descending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isGreaterThan(notes[i + 1].color)
        }
    }
}