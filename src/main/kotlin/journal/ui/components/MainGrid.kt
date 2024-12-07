package journal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import journal.model.MyJournalState
import journal.model.Note
import myjournal.view.components.DarkerGray
import myjournal.view.components.ElevatedDarkGray
import myjournal.view.components.IconColorDefault
import java.awt.Color
import journal.ui.MainPurple as MainPurple1


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
            onClick = {
                viewModel.editNote(Note(id = 0, title = "", body = ""))
            },
            backgroundColor = MainPurple1,
            contentColor = Color.White,
            modifier = Modifier.align(LineHeightStyle.Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Note", modifier = Modifier.size(24.dp)
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


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    note: Note, onClick: () -> Unit, onDelete: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) ElevatedDarkGray else DarkerGray, animationSpec = tween(durationMillis = 300)
    )

    val elevation by animateDpAsState(
        targetValue = if (isHovered) 8.dp else 4.dp, animationSpec = tween(durationMillis = 300)
    )

    Card(elevation = elevation, modifier = Modifier.padding(8.dp).fillMaxWidth().pointerMoveFilter(onEnter = {
        isHovered = true
        false
    }, onExit = {
        isHovered = false
        false
    }), shape = RoundedCornerShape(12.dp), backgroundColor = backgroundColor) {
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
                    style = MaterialTheme.typography.h6.copy(
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
                            text = if (note.id == 0) "Nouvelle entrée" else "Modifier note",
                            style = MaterialTheme.typography.h6,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Title Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = note.title,
                                onValueChange = onTitleChange,
                                textStyle = LocalTextStyle.current.copy(color = Color.White),
                                cursorBrush = SolidColor(MainPurple1),
                                decorationBox = { innerTextField ->
                                    if (note.title.isEmpty()) {
                                        Text(
                                            text = "Titre", style = MaterialTheme.typography.body1, color = Color.Gray
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.weight(1f).padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = note.body,
                                onValueChange = onBodyChange,
                                textStyle = LocalTextStyle.current.copy(color = Color.White),
                                cursorBrush = SolidColor(MainPurple1),
                                decorationBox = { innerTextField ->
                                    if (note.body.isEmpty()) {
                                        Text(
                                            text = "Commencer à écrire...",
                                            style = MaterialTheme.typography.body1,
                                            color = Color.Gray
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.weight(1f).padding(8.dp).heightIn(min = 120.dp)
                            )
                        }
                    }
                }

                Surface(
                    color = DarkerGray, contentColor = Color.White, modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(
                            end = 20.dp
                        )
                    ) {
                        TextButton(
                            onClick = onDismiss, modifier = Modifier.padding(8.dp).height(36.dp)
                        ) {
                            Text("annuler", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onSave, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(
                                //backgroundColor = MainPurple,
                                contentColor = Color.White,
                            ), modifier = Modifier.padding(8.dp).height(36.dp)
                        ) {
                            Text("Sauvegarder", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}





