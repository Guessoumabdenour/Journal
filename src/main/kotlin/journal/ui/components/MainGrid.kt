import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import io.github.alexzhirkevich.compottie.*
import journal.model.MyJournalState
import journal.model.Note
import journal.ui.components.*
import journal.ui.theme.*
import utils.loadJsonFromResources

@Preview
@Composable
fun MainGrid(viewModel: MyJournalState) {
    val notes = viewModel.notes
    val sortedNotes = notes.sortedByDescending { it.id }

    val isEditingNote by remember { derivedStateOf { viewModel.currentNote != null && viewModel.isEditing } }
    var showAnimation by remember { mutableStateOf(true) }
    var isViewingDetails by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Conditionally show the list of notes based on `isViewingDetails` flag
        if (!isViewingDetails && !isEditingNote) {
            // LazyVerticalGrid for showing notes
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 800.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sortedNotes) { note ->
                    NoteCard(
                        note = note,
                        onClickEdit = { viewModel.editNote(note) },
                        onClickDetails = {
                            isViewingDetails = true
                            viewModel.viewNote(note)
                        },
                        onDelete = { viewModel.deleteNote(note) },
                    )
                }
            }
        }

        // Overlay Layer for Animation and FAB
        Box(modifier = Modifier.fillMaxSize()) {
            // Animated Visibility for the Lottie Animation
            AnimatedVisibility(
                visible = showAnimation, // Show animation based on showAnimation state
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-100).dp)
            ) {
                val jsonData = loadJsonFromResources("welcome.json")

                if (jsonData.isNotEmpty()) {
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
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(Color.Red)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !isEditingNote && !isViewingDetails,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(1000.dp))
                    .padding(0.dp)
                    .size(100.dp)
                    .clickable {
                        showAnimation = false
                        viewModel.editNote(Note(id = 0, title = "", body = ""))
                    },
                //shape = MaterialTheme.shapes.extraSmall,
                color = Color.Transparent
            ) {
                val jsonData = loadJsonFromResources("add.json")

                if (jsonData.isNotEmpty()) {
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
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Handle error or display placeholder
                }
            }
        }

        // Show the NoteDialog if a note is being edited
        if (isEditingNote) {
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
                },
                isEditing = isEditingNote // Pass the isEditingNote state here
            )
        }

        // Show the NoteDetailsDialog if a note is being viewed (and not editing)
        if (isViewingDetails && !isEditingNote) {
            NoteDetailsDialog(
                note = viewModel.currentNote!!,
                onDismiss = {
                    isViewingDetails = false // Set to false when dismissed
                    viewModel.clearCurrentNote()
                }
            )
        }
    }
}
