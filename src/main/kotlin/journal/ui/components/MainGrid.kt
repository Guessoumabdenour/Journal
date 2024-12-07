import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.*
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Bookmark
import compose.icons.fontawesomeicons.solid.Bookmark
import journal.model.MyJournalState
import journal.model.Note
import journal.ui.MainPurple
import journal.ui.White
import myjournal.view.components.DarkerGray
import myjournal.view.components.ElevatedDarkGray
import myjournal.view.components.IconColorDefault




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

        FloatingActionButton(
            shape = RoundedCornerShape(100.dp),
            containerColor = MainPurple,
            onClick = {
                viewModel.editNote(Note(id = 0, title = "", body = ""))
            },
            contentColor = White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)

        ) {
            Icon(
                tint = White,
                imageVector = FeatherIcons.Plus,
                contentDescription = "Add Note", modifier = Modifier
                    .background(MainPurple)

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NoteCard(
    note: Note, onClick: () -> Unit, onDelete: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    // Animate background color based on hover state
    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) ElevatedDarkGray else DarkerGray, animationSpec = tween(durationMillis = 300)
    )

    // Animate elevation (shadow) based on hover state
    val elevation by animateDpAsState(
        targetValue = if (isHovered) 8.dp else 4.dp, animationSpec = tween(durationMillis = 300)
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .pointerMoveFilter(onEnter = {
                isHovered = true
                false
            }, onExit = {
                isHovered = false
                false
            }),
        shape = RoundedCornerShape(12.dp),
        //elevation = elevation,  // Apply elevation directly to Card
        //backgroundColor = backgroundColor  // Apply background color directly to Card
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = note.title.ifBlank { "Pas de titre" },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold, color = Color.White
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState())
                )

                Spacer(modifier = Modifier.width(24.dp))

                AnimatedVisibility(
                    visible = isBookmarked,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Bookmark,
                        contentDescription = "Favoris", tint = Color.Red, modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                AnimatedVisibility(
                    visible = isHovered || expanded,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                ) {
                    Box {
                        IconButton(
                            onClick = {
                                expanded = !expanded
                            }, modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = FeatherIcons.MoreHorizontal,
                                contentDescription = "Options supplémentaires",
                                tint = IconColorDefault,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Custom Popup to show dropdown items
                        if (expanded) {
                            Box {
                                Popup(
                                    alignment = Alignment.TopEnd,
                                    onDismissRequest = { expanded = false }  // Close when clicked outside
                                ) {
                                    Box(
                                        modifier = Modifier.background(ElevatedDarkGray, RoundedCornerShape(10.dp))
                                            .width(250.dp).padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 8.dp,
                                                bottom = 8.dp,
                                            )
                                    ) {
                                        Column {
                                            // Dropdown items
                                            DropdownItem(
                                                text = "Editer",
                                                icon = FeatherIcons.Edit,
                                                iconTint = Color.White,
                                                onClick = {
                                                    expanded = false
                                                    onClick()
                                                })

                                            Divider(
                                                color = Color.White.copy(alpha = 0.1f),
                                                thickness = 1.dp,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )

                                            DropdownItem(
                                                text = if (isBookmarked) "Retirer" else "Enregistrer",  // Dynamic text
                                                icon = FontAwesomeIcons.Regular.Bookmark,
                                                iconTint = Color.White, onClick = {
                                                    // Handle the click for "Enregistrer"
                                                    isBookmarked = !isBookmarked  // Toggle bookmark visibility
                                                    expanded = false
                                                })

                                            Divider(
                                                color = Color.White.copy(alpha = 0.1f),
                                                thickness = 1.dp,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )

                                            DropdownItem(
                                                text = "Supprimer",
                                                textColor = Color.Red,  // Red text for the delete option
                                                icon = FeatherIcons.Trash2,
                                                iconTint = Color.Red,
                                                onClick = { onDelete() })
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DropdownItem(
    text: String, icon: ImageVector, iconTint: Color, textColor: Color = Color.White, onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text, color = textColor
        )
        Icon(
            imageVector = icon, contentDescription = text, modifier = Modifier.size(16.dp), tint = iconTint
        )
    }
}

@Composable
fun NoteDialog(
    note: Note,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }
    val transition = updateTransition(targetState = isVisible, label = "Dialog Transition")
    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500, easing = FastOutSlowInEasing) }, label = "Alpha"
    ) { if (it) 1f else 0f }

    Box(
        modifier = Modifier.fillMaxHeight().fillMaxSize().padding(16.dp).alpha(alpha)
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp), color = DarkerGray, modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = if (note.id == 0) "Ajouter une note" else "Modifier la note",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        BasicTextField(
                            value = note.title,
                            onValueChange = onTitleChange,
                            decorationBox = { innerTextField ->
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    if (note.title.isEmpty()) {
                                        Text(
                                            text = "Titre",
                                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        BasicTextField(
                            value = note.body,
                            onValueChange = onBodyChange,
                            decorationBox = { innerTextField ->
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    if (note.body.isEmpty()) {
                                        Text(
                                            text = "Corps de la note...",
                                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                            maxLines = 5
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss, colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                    ) {
                        Text("Annuler")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = onSave, colors = ButtonDefaults.buttonColors(containerColor = MainPurple)
                    ) {
                        Text("Sauvegarder")
                    }
                }
            }
        }
    }
}
