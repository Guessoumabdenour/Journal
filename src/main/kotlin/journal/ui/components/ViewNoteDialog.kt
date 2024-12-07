package journal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.X
import journal.model.Note

@Composable
fun ViewNoteDialog(
    note: Note,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent) // Semi-transparent background
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp), // Adjusted shape
            color = DarkerGray,
            modifier = Modifier
                .fillMaxHeight(0.8f) // Adjust height proportionally
                .fillMaxWidth() // Adjustwidth
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp) // Unified padding
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Title Text
                    Text(
                        text = note.title.ifBlank { "Pas de titre" },
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.CenterStart) // Ensure proper alignment
                    )

                    // Close Button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd) // Align to top-right
                            .padding(8.dp) // Adjust padding
                    ) {
                        Icon(
                            imageVector = FeatherIcons.X,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                // Divider
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White.copy(alpha = 0.1f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Body
                Text(
                    text = note.body.ifBlank { "Pas de contenu" },
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Date
                Text(
                    text = note.dateCreated ?: "Date inconnue",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }
    }
}