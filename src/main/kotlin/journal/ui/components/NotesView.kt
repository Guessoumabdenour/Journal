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

    // Determine if a note is being added or edited/viewed
    val isAddingOrEditingNote by remember { derivedStateOf { viewModel.currentNote != null } }

    var showAnimation by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
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

        if (isAddingOrEditingNote) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkerGray)
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = showAnimation,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-50).dp) // Move animation 100.dp upwards
            ) {
                // Load the JSON content from the resources directory
                val jsonData = loadJsonFromResources("welcome.json") // Path to your JSON file

                if (jsonData.isNotEmpty()) {
                    // Create Lottie composition from JSON data
                    val composition by rememberLottieComposition {
                        LottieCompositionSpec.JsonString(jsonData)
                    }

                    val progress by animateLottieCompositionAsState(
                        composition,
                        iterations = Compottie.IterateForever
                    )

                    Image(
                        painter = rememberLottiePainter(
                            composition = composition,
                            progress = { progress }
                        ),
                        contentDescription = "Lottie animation",
                        modifier = Modifier.size(600.dp)
                    )
                } else {
                    // Fallback in case JSON fails to load
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(Color.Red)
                    )
                }
            }

            // Floating Action Button for adding a new note
            // The FAB is only visible when no note is being added or edited/viewed
            AnimatedVisibility(
                visible = !isAddingOrEditingNote,
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
                        // Set showAnimation to false to hide the animation permanently
                        showAnimation = false
                        // Proceed to add/edit a new note
                        viewModel.editNote(Note(id = 0, title = "", body = ""))
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
        }

        // Show the NoteDialog if a note is being edited or viewed
        if (isAddingOrEditingNote) {
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
