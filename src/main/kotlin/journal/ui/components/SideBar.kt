package journal.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.*
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.CalendarAlt
import compose.icons.fontawesomeicons.solid.QuoteLeft
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.fontawesomeicons.regular.CalendarCheck
import journal.model.Note


val DarkerGray = Color(0xFF1C1C1C)
val ElevatedDarkGray = Color(0xFF333333)
val WhiteColor = Color.White
val IconColorDefault = Color(0xFFFFFFFF)
val TextColorDefault = Color(0xFFFFFFFF)
val SublabelColorDefault = Color(0xFFAAAAAA)

@Composable
fun SideBar(
    modifier: Modifier = Modifier.clip(RoundedCornerShape(20.dp)),
    notes: List<Note>, // Accept notes as a parameter
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxHeight()
            .width(250.dp)
            .background(DarkerGray)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, bottom = 24.dp, top = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Journal",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = WhiteColor, fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 0.dp, bottom = 12.dp)
            )

            CustomButton(
                icon = FontAwesomeIcons.Regular.CalendarCheck,
                label = "1",
                sublabel = "entrée cette année",
                iconColor = Color(0xFF5E5BE6),
            )

            Spacer(modifier = Modifier.height(6.dp))

            CustomButton(
                icon = FontAwesomeIcons.Solid.QuoteLeft,
                label = "11",
                sublabel = "Mots écrits",
                iconColor = Color(0xFFC36D73),
            )

            Spacer(modifier = Modifier.height(6.dp))

            CustomButton(
                icon = FontAwesomeIcons.Regular.CalendarAlt,
                label = "1",
                sublabel = "jour d'écriture",
                iconColor = Color(0xFF7209b7),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

            // Add the Notes List dynamically from the notes parameter
            Text(
                text = "Notes récentes",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = WhiteColor, fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(
                    top = 8.dp, bottom = 8.dp
                )
            )

            NotesList(notes = notes) // Display the actual notes here

            Spacer(modifier = Modifier.weight(1f)) // Pushes the settings to the bottom

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

                Text(
                    text = "Paramètres",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = WhiteColor, fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                SettingsItem(
                    icon = FeatherIcons.CloudOff,
                    label = "Synchronisation",
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(8.dp))

                SettingsItem(
                    icon = FeatherIcons.Award,
                    label = "Passer à premium",
                    modifier = Modifier
                )
            }

        }

    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = SublabelColorDefault,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = SublabelColorDefault
            )
        )
        Spacer(modifier = Modifier.width(16.dp)) // Espacement entre l'icône et le texte
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NotesList(notes: List<Note>) {
    Column {
        notes.forEach { note ->
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(note.id) {
                isVisible = true
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                Text(
                    text = note.title.ifBlank { "Pas de titre" },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    ),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .animateEnterExit(
                            enter = fadeIn(),
                            exit = fadeOut()
                        )
                )
            }
        }
    }
}
