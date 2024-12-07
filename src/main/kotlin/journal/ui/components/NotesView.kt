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
import journal.ui.MainPurple
import journal.ui.White
import journal.ui.components.*


@Composable
fun MainGrid(viewModel: MyJournalState) {

    val notes = viewModel.notes
    val sortedNotes = notes.sortedByDescending { it.id }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 600.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(sortedNotes) { note ->
                NoteCard(note = note, onClick = { viewModel.editNote(note) }, onDelete = {
                    viewModel.deleteNote(note)
                })
            }
        }

        if (viewModel.currentNote != null) {
            Box(
                modifier = Modifier.fillMaxSize().background(DarkerGray)
            )
        }

        // Add button
        FloatingActionButton(
            shape = RoundedCornerShape(100.dp), containerColor = MainPurple, onClick = {
                viewModel.editNote(Note(id = 0, title = "", body = ""))
            }, contentColor = White, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)

        ) {
            Icon(
                tint = White,
                imageVector = FeatherIcons.Plus,
                contentDescription = "Add Note",
                modifier = Modifier.background(MainPurple)

                    .size(24.dp)
            )
        }

        if (viewModel.currentNote != null) {
            NoteDialog(note = viewModel.currentNote!!, onSave = {
                if (viewModel.currentNote!!.id == 0) {
                    viewModel.addNote(viewModel.currentNote!!.title, viewModel.currentNote!!.body)
                } else {
                    viewModel.updateNote(viewModel.currentNote!!)
                }
                viewModel.clearCurrentNote()
            }, onDismiss = { viewModel.clearCurrentNote() }, onTitleChange = { newTitle ->
                viewModel.currentNote = viewModel.currentNote?.copy(title = newTitle)
            }, onBodyChange = { newBody ->
                viewModel.currentNote = viewModel.currentNote?.copy(body = newBody)
            })
        }
    }
}






