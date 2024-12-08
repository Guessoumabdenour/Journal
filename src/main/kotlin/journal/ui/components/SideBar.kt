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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextOverflow
import compose.icons.fontawesomeicons.regular.CalendarCheck
import journal.model.MyJournalState
import journal.model.Note
import journal.ui.theme.*

import java.text.SimpleDateFormat
import java.util.*






@Composable
fun SideBar(
    modifier: Modifier = Modifier.clip(RoundedCornerShape(20.dp)),
    notes: List<Note>,
    myJournalState: MyJournalState

) {
    val (entriesThisYear, wordsWritten, writingDays) = calculateCounts(myJournalState.notes)

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
                    color = WhiteColor,
                    fontWeight = FontWeight.SemiBold

                ),
                modifier = Modifier.padding(top = 0.dp, bottom = 12.dp)
            )

            // Dynamically pass the count values
            CustomBox(
                icon = FontAwesomeIcons.Regular.CalendarCheck,
                label = entriesThisYear.toString(),
                sublabel = "entrée cette année",
                iconColor = Color(0xFF5E5BE6),
            )

            Spacer(modifier = Modifier.height(6.dp))

            CustomBox(
                icon = FontAwesomeIcons.Solid.QuoteLeft,
                label = wordsWritten.toString(),
                sublabel = "Mots écrits",
                iconColor = Color(0xFFC36D73),
            )

            Spacer(modifier = Modifier.height(6.dp))

            CustomBox(
                icon = FontAwesomeIcons.Regular.CalendarAlt,
                label = writingDays.toString(),
                sublabel = "jour d'écriture",
                iconColor = Color(0xFF7209b7),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

            // Add the Notes List dynamically from the notes parameter
            Text(
                text = "Notes récentes",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = WhiteColor,                                     fontWeight = FontWeight.SemiBold

                ),
                modifier = Modifier.padding(
                    top = 8.dp, bottom = 8.dp
                )
            )

            // Box to wrap the LazyColumn and Spacer
            Box(modifier = Modifier.weight(1f)) {
                // LazyColumn for the notes list
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(notes) { note ->
                        var isVisible by remember { mutableStateOf(false) }

                        LaunchedEffect(note.id) {
                            isVisible = true
                        }

                        this@Column.AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                            exit = fadeOut(animationSpec = tween(durationMillis = 500))
                        ) {
                            NoteItem(note = note)
                        }
                    }
                }
            }

            // Spacer to push the settings to the bottom
            Spacer(modifier = Modifier.height(8.dp))

            // Settings section stays fixed at the bottom
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
                        color = WhiteColor,                                     fontWeight = FontWeight.SemiBold

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
fun calculateCounts(notes: List<Note>): Triple<Int, Int, Int> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val entriesThisYear = notes.count {
        val noteDate = SimpleDateFormat("EEEE d MMMM", Locale.FRENCH).parse(it.dateCreated)
        val noteYear = Calendar.getInstance().apply { time = noteDate }.get(Calendar.YEAR)
        noteYear == currentYear
    }

    val wordsWritten = notes.sumOf { note ->
        note.body.split("\\s+".toRegex()).size
    }

    val distinctWritingDays = notes.map { note ->
        val noteDate = SimpleDateFormat("EEEE d MMMM", Locale.FRENCH).parse(note.dateCreated)
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(noteDate)
    }.toSet().size

    return Triple(entriesThisYear, wordsWritten, distinctWritingDays)
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
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteItem(note: Note) {
    var isHovered by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isHovered) ElevatedDarkGray
                else DarkerGray
            )
            .onPointerEvent(PointerEventType.Enter) {
                isHovered = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                isHovered = false
            }
            .padding(8.dp)
    ) {
        LaunchedEffect(isHovered) {
            if (isHovered) {
                scrollState.animateScrollTo(0)
            } else {
                scrollState.animateScrollTo(0)
            }
        }

        Text(
            text = note.title.ifBlank { "Pas de titre" },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .horizontalScroll(scrollState)
                .animateContentSize(tween(durationMillis = 300))
        )
    }
}



