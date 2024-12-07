package myjournal.view.components

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
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextOverflow
import compose.icons.fontawesomeicons.regular.CalendarCheck
import journal.ui.components.CustomButton


val DarkerGray = Color(0xFF1C1C1C)
val ElevatedDarkGray = Color(0xFF333333)
val WhiteColor = Color.White
val IconColorDefault = Color(0xFFFFFFFF)
val TextColorDefault = Color(0xFFFFFFFF)
val SublabelColorDefault = Color(0xFFAAAAAA)

@Composable
fun LeftNavigationBar(
    modifier: Modifier = Modifier.clip(RoundedCornerShape(20.dp))
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(10.dp)).fillMaxHeight().width(250.dp).background(DarkerGray)
    ) {
        Column(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 24.dp, top = 24.dp)
                .clip(RoundedCornerShape(20.dp)).fillMaxSize(), verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Journal", style = MaterialTheme.typography.headlineSmall.copy(
                    color = WhiteColor, fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(top = 0.dp, bottom = 12.dp)
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

            androidx.compose.material.Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

            Text(
                text = "Notes récentes", style = MaterialTheme.typography.headlineSmall.copy(
                    color = WhiteColor, fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(
                    top = 8.dp, bottom = 8.dp
                )
            )

            NotesList(
                notes = listOf(
                    "Titre de la note",
                )
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                androidx.compose.material.Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

                Text(
                    text = "Paramètres", style = MaterialTheme.typography.headlineSmall.copy(
                        color = WhiteColor, fontWeight = FontWeight.Bold
                    ), modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = FeatherIcons.CloudOff,
                        contentDescription = "Synchronisation",
                        tint = SublabelColorDefault,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Synchronisation", style = MaterialTheme.typography.bodyLarge.copy(
                            color = SublabelColorDefault
                        )
                    )
                    Spacer(modifier = Modifier.width(16.dp)) // Espacement entre l'icône et le texte

                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = FeatherIcons.Award,
                        contentDescription = "Passer à premium",
                        tint = SublabelColorDefault,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Passer à premium", style = MaterialTheme.typography.bodyLarge.copy(
                            color = SublabelColorDefault
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                }
            }

        }

    }
}


@Composable
fun NotesList(notes: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        items(notes) { note ->
            NoteItem(note = note)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteItem(
    note: String
) {
    var isHovered by remember { mutableStateOf(false) } // État de survol
    var expanded by remember { mutableStateOf(false) } // État du menu déroulant

    // Transition de couleur de fond
    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) ElevatedDarkGray else DarkerGray, animationSpec = tween(durationMillis = 300)
    )

    // Transition d'élévation
    val elevation by animateDpAsState(
        targetValue = if (isHovered) 8.dp else 2.dp, animationSpec = tween(durationMillis = 300)
    )

    Surface(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).pointerMoveFilter(onEnter = {
        isHovered = true
        false
    }, onExit = {
        isHovered = false
        false
    }), color = backgroundColor, shadowElevation = elevation, shape = RoundedCornerShape(0.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            // Texte de la note avec poids pour occuper l'espace restant
            Text(
                text = note,
                color = WhiteColor,  // Garde le texte en blanc
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                maxLines = 1, // Assure qu'une seule ligne est utilisée pour le titre
                overflow = TextOverflow.Ellipsis, // Tronque le texte débordant avec des points de suspension
                modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState())
            )
        }
    }
}