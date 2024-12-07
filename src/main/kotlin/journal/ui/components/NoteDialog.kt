package journal.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import journal.model.Note
import journal.ui.MainPurple
import journal.ui.White

@Composable
fun NoteDialog(
    note: Note,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit
) {
    val isVisible by remember { mutableStateOf(true) }
    val transition = updateTransition(targetState = isVisible, label = "Dialog Transition")
    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500, easing = FastOutSlowInEasing) },
        label = "Alpha"
    ) { if (it) 1f else 0f }

    Box(
        modifier = Modifier
            .fillMaxSize()  // Make the Box take up the whole screen
            .padding(16.dp)
            .alpha(alpha)  // Handle fade-in/out animation
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = DarkerGray,
            modifier = Modifier
                .fillMaxSize()  // Make sure the Surface fills the screen
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()  // Ensure the column fills the available height
            ) {
                // Content area that takes up most of the space (scrollable)
                Box(
                    modifier = Modifier
                        .weight(1f)  // Ensure it fills the remaining space
                        .verticalScroll(rememberScrollState())  // Make content scrollable
                        .padding(16.dp)
                ) {
                    Column {
                        // Title section
                        Text(
                            text = if (note.id == 0) "Ajouter une note" else "Modifier la note",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Title input field
                        BasicTextField(
                            value = note.title,
                            onValueChange = onTitleChange,
                            cursorBrush = SolidColor(MainPurple),
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
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Divider between title and body
                        Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Body input field - make it take full remaining height
                        BasicTextField(
                            value = note.body,
                            onValueChange = onBodyChange,
                            cursorBrush = SolidColor(MainPurple),
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
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                            maxLines = Int.MAX_VALUE,  // Let it expand indefinitely
                            modifier = Modifier
                                .fillMaxHeight(0.5f)  // Take up 50% of the available height
                                .padding(vertical = 8.dp)  // Add padding to make it visually appealing
                        )
                    }
                }

                // Button row at the bottom (Save and Cancel)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),  // Add padding to the buttons
                    horizontalArrangement = Arrangement.End
                ) {
                    // Cancel button
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                    ) {
                        Text("Annuler")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Save button
                    Button(
                        onClick = onSave,
                        colors = ButtonDefaults.buttonColors(containerColor = MainPurple)
                    ) {
                        Text("Sauvegarder")
                    }
                }
            }
        }
    }
}
