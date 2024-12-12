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
import journal.ui.theme.*


@Composable
fun NoteDialog(
    note: Note,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    isEditing: Boolean
) {
    val transition = updateTransition(targetState = true, label = "Dialog Transition")
    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500, easing = FastOutSlowInEasing) },
        label = "Alpha"
    ) { 1f }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = DarkerGray,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = if (note.id == 0) "Ajouter une note" else "Modifier la note",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

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

                        Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

                        Spacer(modifier = Modifier.height(16.dp))

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
                            maxLines = Int.MAX_VALUE,
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .padding(vertical = 8.dp)
                        )
                    }
                }

                // Show save/cancel buttons only if in edit mode
                if (isEditing) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                        ) {
                            Text("Annuler")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

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
}


@Composable
fun NoteDetailsDialog(
    note: Note,
    onDismiss: () -> Unit
) {
    val transition = updateTransition(targetState = true, label = "Dialog Transition")
    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500, easing = FastOutSlowInEasing) },
        label = "Alpha"
    ) { 1f }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = DarkerGray,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Détails de la note",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = note.body,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MainPurple)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}







