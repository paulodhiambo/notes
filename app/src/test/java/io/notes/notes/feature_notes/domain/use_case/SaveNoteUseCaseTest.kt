package io.notes.notes.feature_notes.domain.use_case

import com.google.common.truth.Truth.assertThat
import io.notes.notes.feature_notes.data.repository.FakeNoteRepository
import io.notes.notes.feature_notes.domain.model.InvalidNoteException
import io.notes.notes.feature_notes.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlin.random.Random


class SaveNoteUseCaseTest {
    private lateinit var saveNoteUseCase: SaveNoteUseCase
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        saveNoteUseCase = SaveNoteUseCase(fakeNoteRepository)
    }

    @Test
    fun `Save valid note`() {
        val note = Note(
            title = "Note title",
            content = "Note content",
            timeStamp = Date().time,
            color = Random(100).nextInt(),
            id = 1
        )
        runBlocking {
            saveNoteUseCase(note)
            assertThat(fakeNoteRepository.getNote(1)).isNotNull()
        }
    }

    @Test
    fun `Save note with blank title`() {
        val note = Note(
            title = "",
            content = "Note content",
            timeStamp = Date().time,
            color = Random(100).nextInt()
        )
        assertThrows(InvalidNoteException::class.java) {
            runBlocking {
                saveNoteUseCase(note)
            }
        }
    }

    @Test
    fun `Save note with blank content`() {
        val note = Note(
            title = "",
            content = "Note content",
            timeStamp = Date().time,
            color = Random(100).nextInt()
        )
        assertThrows(InvalidNoteException::class.java) {
            runBlocking {
                saveNoteUseCase(note)
            }
        }
    }

    @Test
    fun `Save note with blank content and title`() {
        val note = Note(
            title = "",
            content = "",
            timeStamp = Date().time,
            color = Random(100).nextInt()
        )
        assertThrows(InvalidNoteException::class.java) {
            runBlocking {
                saveNoteUseCase(note)
            }
        }
    }
}