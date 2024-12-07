package journal.ui

import MainGrid
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.CloudOff
import compose.icons.feathericons.Edit
import compose.icons.feathericons.MoreHorizontal
import compose.icons.feathericons.Trash2
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Bookmark
import compose.icons.fontawesomeicons.solid.Bookmark
import journal.model.MyJournalState
import journal.model.MyJournalState.Companion.getCurrentDate
import journal.model.Note
import myjournal.view.components.DarkerGray
import myjournal.view.components.ElevatedDarkGray
import myjournal.view.components.IconColorDefault
import myjournal.view.components.LeftNavigationBar

import java.util.*

val DarkGray = Color(0xFF2F2F2F)
val White = Color(0xFFFFFFFF)
val LightPurple = Color(0xFF301E34)
val DarkPurple = Color(0xFF080812)
val MainPurple = Color(0xFF5E5BE6)

@Preview
@Composable
fun App(viewModel: MyJournalState = MyJournalState()) {


    val currentDate = remember { getCurrentDate() }
    val focusRequester = remember { FocusRequester() }
    var isAddingNote by remember { mutableStateOf(false) }
    var newNoteTitle by remember { mutableStateOf("") }
    var newNoteBody by remember { mutableStateOf("") }

    MaterialTheme {
        Box(Modifier.fillMaxSize().focusRequester(focusRequester).focusable().onKeyEvent { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.isCtrlPressed && keyEvent.key == Key.S) {

                viewModel.currentNote?.let {
                    if (it.id == 0) {
                        if (it.title.isNotBlank() || it.body.isNotBlank()) {
                            viewModel.addNote(it.title, it.body)
                            viewModel.clearCurrentNote()
                        }
                    } else {
                        viewModel.updateNote(it)
                        viewModel.clearCurrentNote()
                    }
                }
                true
            } else {
                false
            }
        }) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(Brush.verticalGradient(colors = listOf(DarkPurple, LightPurple))),
            ) {

                Row(
                    modifier = Modifier.weight(1f)
                ) {

                    LeftNavigationBar(
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).padding(8.dp)
                    )

                    Column(
                        modifier = Modifier.padding(
                            top = 8.dp, bottom = 8.dp, end = 8.dp
                        ).clip(RoundedCornerShape(10.dp)).fillMaxSize().weight(1f).background(DarkerGray),

                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.padding(16.dp).clip(RoundedCornerShape(10.dp)).height(60.dp)
                                .fillMaxWidth(), color = DarkGray.copy(alpha = 0.8f), shape = RoundedCornerShape(10.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "Bonjour,  \uD83D\uDE0A ",
                                        style = TextStyle(
                                            fontSize = 24.sp, color = White, fontWeight = FontWeight.Bold
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )

                                    //Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = currentDate, style = TextStyle(
                                            fontSize = 24.sp, color = White, fontWeight = FontWeight.Bold
                                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }


                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text(
                                text = "Aujourd'hui",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = White, fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(start = 12.dp, top = 0.dp, bottom = 0.dp)
                            )
                        }

                        MainGrid(viewModel = viewModel)

                        // Conditional Add Note Form
                        if (isAddingNote) {
                            AddNoteForm(
                                title = newNoteTitle,
                                onTitleChange = { newNoteTitle = it },
                                body = newNoteBody,
                                onBodyChange = { newNoteBody = it },
                                onSave = {
                                    viewModel.addNote(newNoteTitle, newNoteBody)
                                    // Reset the form
                                    newNoteTitle = ""
                                    newNoteBody = ""
                                    isAddingNote = false
                                },
                                onCancel = {
                                    // Reset the form
                                    newNoteTitle = ""
                                    newNoteBody = ""
                                    isAddingNote = false
                                })
                        }

                        // Floating Add Button
                        Box(
                            modifier = Modifier.fillMaxSize().padding(16.dp), // Adjust padding as needed
                        ) {
                            Button(
                                onClick = { isAddingNote = true }, // Show the Add Note form
                                modifier = Modifier.align(Alignment.BottomEnd) // Positioning at bottom right
                                    .size(56.dp) // Size of the round button
                                    .clip(CircleShape), colors = ButtonDefaults.buttonColors(
                                    //backgroundColor = MainPurple, // Background color
                                    contentColor = White // Icon color (or text if using text button)
                                ),// Round shape
                                contentPadding = PaddingValues(0.dp) // Remove padding inside the button
                            ) {
                                Icon(
                                    imageVector = FeatherIcons.MoreHorizontal,
                                    contentDescription = "Add",
                                    modifier = Modifier.size(24.dp),
                                    tint = White
                                )
                            }
                        }
                    }
                }

            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun AddNoteForm(
    title: String,
    onTitleChange: (String) -> Unit,
    body: String,
    onBodyChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color(0xFF1E1E1E), RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Ajouter une nouvelle note", style = TextStyle(
                fontSize = 20.sp, color = White, fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Titre") },
            modifier = Modifier.fillMaxWidth().background(Color(0xFF2F2F2F), RoundedCornerShape(5.dp)),
            colors = TextFieldDefaults.textFieldColors(
                //backgroundColor = Color.Transparent,
                textColor = White,
                cursorColor = MainPurple,
                focusedIndicatorColor = MainPurple,
                unfocusedIndicatorColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = body,
            onValueChange = onBodyChange,
            label = { Text("Contenu") },
            modifier = Modifier.fillMaxWidth().fillMaxHeight()

                .height(600.dp).background(Color(0xFF2F2F2F), RoundedCornerShape(5.dp)),
            colors = TextFieldDefaults.textFieldColors(
                //backgroundColor = Color.Transparent,
                textColor = White,
                cursorColor = MainPurple,
                focusedIndicatorColor = MainPurple,
                unfocusedIndicatorColor = Color.Gray
            ),
            maxLines = 10
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onCancel) {
                Text("Annuler", color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSave, colors = ButtonDefaults.buttonColors(MainPurple)
            ) {
                Text("Enregistrer", color = White)
            }
        }
    }
}














