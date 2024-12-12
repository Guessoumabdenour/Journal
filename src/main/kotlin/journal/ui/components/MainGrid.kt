package journal.ui.components

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.*
import journal.model.MyJournalState
import journal.model.Note
import utils.loadJsonFromResources

@Composable
fun MainGrid(viewModel: MyJournalState) {

    val notes = viewModel.notes
    val sortedNotes = notes.sortedByDescending { it.id }
    val isEditingNote by remember { derivedStateOf { viewModel.currentNote != null && viewModel.isEditing } }
    var showAnimation by remember { mutableStateOf(true) }
    var isViewingDetails by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isViewingDetails && !isEditingNote) {
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

        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = showAnimation,
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
                    .clip(RoundedCornerShape(100.dp))
                    .wrapContentSize()
                    .clickable {
                        showAnimation = false
                        viewModel.editNote(Note(id = 0, title = "", body = ""))
                    },
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
                            .size(80.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        if (isEditingNote) {
            NoteDialog(
                note = viewModel.currentNote!!,
                onSave = {
                    if (viewModel.currentNote!!.id == 0) {
                        viewModel.addNote(viewModel.currentNote!!.title, viewModel.currentNote!!.body)
                    } else {
                        viewModel.updateNote(viewModel.currentNote!!)
                    }
                    viewModel.clearCurrentNote()
                },
                onDismiss = { viewModel.clearCurrentNote() },
                onTitleChange = { newTitle ->
                    viewModel.currentNote = viewModel.currentNote?.copy(title = newTitle)
                },
                onBodyChange = { newBody ->
                    viewModel.currentNote = viewModel.currentNote?.copy(body = newBody)
                },
                isEditing = isEditingNote
            )
        }

        if (isViewingDetails && !isEditingNote) {
            NoteDetailsDialog(
                note = viewModel.currentNote!!,
                onDismiss = {
                    isViewingDetails = false
                    viewModel.clearCurrentNote()
                }
            )
        }
    }
}
