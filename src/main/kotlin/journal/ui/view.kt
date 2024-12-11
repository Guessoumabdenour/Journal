package journal.ui

import MainGrid
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.compottie.*
import journal.model.MyJournalState
import journal.model.MyJournalState.Companion.getCurrentDate
import journal.ui.components.SideBar
import journal.ui.theme.*
import utils.loadJsonFromResources

@Preview
@Composable
fun App(viewModel: MyJournalState = MyJournalState()) {

    val sortedNotes by remember { derivedStateOf { viewModel.notes.sortedByDescending { it.id } } }
    val currentDate = remember { getCurrentDate() }
    val focusRequester = remember { FocusRequester() }

    val jsonData = loadJsonFromResources("hello.json") // Path to your JSON file
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(jsonData)
    }

    val progress by animateLottieCompositionAsState(
        composition, iterations = Compottie.IterateForever
    )
    var isAddingNote by remember { mutableStateOf(false) }
    var newNoteTitle by remember { mutableStateOf("") }
    var newNoteBody by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }


    MaterialTheme {
        Box(
            Modifier.fillMaxSize().focusRequester(focusRequester).focusable().onKeyEvent { keyEvent ->
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
                modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            colors = listOf(DarkPurple, LightPurple)
                        )
                    )
            ) {

                Row(modifier = Modifier.weight(1f)) {

                    SideBar(
                        notes = sortedNotes,
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).padding(8.dp),
                        myJournalState = viewModel
                    )

                    Column(
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                            .clip(RoundedCornerShape(10.dp)).fillMaxSize().weight(1f).background(DarkerGray),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Surface(
                            modifier = Modifier.padding(16.dp).clip(RoundedCornerShape(10.dp)).height(80.dp)
                                .fillMaxWidth(), color = DarkGray.copy(alpha = 0.2f), shape = RoundedCornerShape(10.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier.fillMaxSize().padding(
                                            end = 24.dp, start = 24.dp, top = 8.dp, bottom = 8.dp
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    // Greeting and Date
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = rememberLottiePainter(
                                            composition = composition, progress = { progress }),
                                            contentDescription = "Lottie animation",
                                            modifier = Modifier.size(200.dp),
                                            contentScale = ContentScale.FillWidth)
                                        Spacer(modifier = Modifier.width(16.dp)) // Adjusted spacing

                                        // **2. Add Search Bar**
                                        TextField(
                                            value = searchQuery,
                                            onValueChange = { searchQuery = it },
                                            placeholder = { Text("Search notes") },
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "Search Icon",
                                                    tint = White
                                                )
                                            },
                                            singleLine = true,
                                            colors = TextFieldDefaults.textFieldColors(
                                                containerColor = ElevatedDarkerGray,
                                                cursorColor = MainPurple,
                                                textColor = White,
                                                placeholderColor = White.copy(alpha = 0.5f),
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent
                                            ),
                                            modifier = Modifier.clip(RoundedCornerShape(20.dp)) // Rounded corners for aesthetics
                                                .background(DarkerGray)
                                                .weight(1f) // Allows the TextField to take up remaining horizontal space
                                                .height(50.dp)
                                        )

                                        Spacer(modifier = Modifier.width(16.dp)) // Adjusted spacing

                                        Text(
                                            text = currentDate, style = TextStyle(
                                                fontSize = 24.sp, color = White, fontWeight = FontWeight.Medium
                                            ), maxLines = 1, overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        // Main Grid displaying all notes
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
                textColor = White,
                cursorColor = MainPurple,
                focusedIndicatorColor = MainPurple,
                unfocusedIndicatorColor = Color.Gray,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = body,
            onValueChange = onBodyChange,
            label = { Text("Contenu") },
            modifier = Modifier.fillMaxWidth().height(200.dp).background(Color(0xFF2F2F2F), RoundedCornerShape(5.dp)),
            colors = TextFieldDefaults.textFieldColors(
                textColor = White,
                cursorColor = MainPurple,
                focusedIndicatorColor = MainPurple,
                unfocusedIndicatorColor = Color.Gray,
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
                onClick = onSave, colors = ButtonDefaults.buttonColors(containerColor = MainPurple)
            ) {
                Text("Enregistrer", color = White)
            }
        }
    }
}
