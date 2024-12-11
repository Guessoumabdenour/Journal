package journal.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import journal.model.MyJournalState.Companion.getCurrentDate
import java.text.SimpleDateFormat
import java.util.*

data class Note(
    val id: Int,
    var title: String,
    var body: String,
    val dateCreated: String = getCurrentDate()
)

class MyJournalState {

    val notes = mutableStateListOf<Note>()
    var currentNote by mutableStateOf<Note?>(null)
    var isEditing = false // Flag to track editing mode

    fun addNote(title: String, body: String) {
        val newId = if (notes.isEmpty()) 1 else notes.maxOf { it.id } + 1
        val note = Note(id = newId, title = title, body = body)
        notes.add(note)
    }

    fun deleteNote(note: Note) {
        notes.remove(note)
    }

    fun updateNote(updatedNote: Note) {
        val index = notes.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            notes[index] = updatedNote
        }
    }

    fun editNote(note: Note) {
        currentNote = note
        isEditing = true // Set editing mode
    }

    fun viewNote(note: Note) {
        currentNote = note
        isEditing = false // Set to viewing mode
    }

    fun clearCurrentNote() {
        currentNote = null
        isEditing = false // Clear the editing/viewing state
    }

    companion object {
        fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("EEEE d MMMM", Locale.FRENCH)
            return dateFormat.format(calendar.time)
        }
    }
}
