import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import io.github.alexzhirkevich.compottie.*
import journal.model.MyJournalState
import journal.model.Note
import journal.ui.components.*
import journal.ui.theme.*
import utils.loadJsonFromResources

@Composable
fun MainGrid(viewModel: MyJournalState) {
    val notes = viewModel.notes
    val sortedNotes = notes.sortedByDescending { it.id }

    // Boolean flag to check if a note is being viewed or edited
    val isViewingOrEditingNote by remember { derivedStateOf { viewModel.currentNote != null } }

    var showAnimation by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Conditionally show the list of notes based on `isViewingOrEditingNote` flag
        if (!isViewingOrEditingNote) {
            // LazyVerticalGrid for showing notes
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 800.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sortedNotes) { note ->
                    NoteCard(
                        note = note,
                        onClick = { viewModel.editNote(note) },  // Handle note viewing/editing
                        onDelete = { viewModel.deleteNote(note) }
                    )
                }
            }
        }

        // Floating Action Button for adding a new note
        AnimatedVisibility(
            visible = !isViewingOrEditingNote,  // Only visible when no note is being added/edited
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                shape = RoundedCornerShape(100.dp),
                containerColor = MainPurple,
                onClick = {
                    showAnimation = false  // Hide the animation
                    viewModel.editNote(Note(id = 0, title = "", body = ""))  // Start editing a new note
                },
                contentColor = White
            ) {
                Icon(
                    tint = White,
                    imageVector = FeatherIcons.Plus,
                    contentDescription = "Add Note",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Show the NoteDialog if a note is being edited or viewed
        if (isViewingOrEditingNote) {
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

