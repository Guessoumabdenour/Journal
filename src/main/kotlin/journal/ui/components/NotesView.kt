import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import journal.model.MyJournalState
import journal.model.Note
import journal.ui.components.*
import journal.ui.theme.*



@Composable
fun MainGrid(viewModel: MyJournalState) {

    val notes = viewModel.notes
    val sortedNotes = notes.sortedByDescending { it.id }

    Box(modifier = Modifier.fillMaxSize()) {
        // LazyVerticalGrid to display notes in a grid format
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 600.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(sortedNotes) { note ->
                NoteCard(
                    note = note,
                    onClick = { viewModel.editNote(note) },
                    onDelete = {
                        viewModel.deleteNote(note)
                    }
                )
            }
        }

        // Background overlay for dialog
        if (viewModel.currentNote != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkerGray.copy(alpha = 0.7f))  // Semi-transparent background for dialog
            )
        }

        // Floating Action Button for adding a new note
        FloatingActionButton(
            shape = RoundedCornerShape(100.dp),
            containerColor = MainPurple,
            onClick = {
                viewModel.editNote(Note(id = 0, title = "", body = ""))  // Edit new empty note
            },
            contentColor = White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                tint = White,
                imageVector = FeatherIcons.Plus,
                contentDescription = "Add Note",
                modifier = Modifier
                    .size(24.dp)
            )
        }

        // Show the NoteDialog if a note is being edited
        if (viewModel.currentNote != null) {
            NoteDialog(
                note = viewModel.currentNote!!,
                onSave = {
                    if (viewModel.currentNote!!.id == 0) {
                        viewModel.addNote(viewModel.currentNote!!.title, viewModel.currentNote!!.body)
                    } else {
                        viewModel.updateNote(viewModel.currentNote!!)
                    }
                    viewModel.clearCurrentNote()  // Clear the current note after saving
                },
                onDismiss = { viewModel.clearCurrentNote() },  // Dismiss and clear the current note
                onTitleChange = { newTitle ->
                    viewModel.currentNote = viewModel.currentNote?.copy(title = newTitle)
                },
                onBodyChange = { newBody ->
                    viewModel.currentNote = viewModel.currentNote?.copy(body = newBody)
                }
            )
        }
    }
}







