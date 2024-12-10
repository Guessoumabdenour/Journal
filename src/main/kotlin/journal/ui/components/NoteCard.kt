package journal.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.Edit
import compose.icons.feathericons.MoreHorizontal
import compose.icons.feathericons.Trash2
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Bookmark
import compose.icons.fontawesomeicons.solid.Bookmark
import journal.model.Note
import journal.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) ElevatedDarkGray else DarkerGray, animationSpec = tween(durationMillis = 300)
    )
    val elevation by animateDpAsState(
        targetValue = if (isHovered) 8.dp else 4.dp, animationSpec = tween(durationMillis = 300)
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .onPointerEvent(PointerEventType.Move) { }
            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
            .clickable { showDialog = true },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(elevation),
    ) {
        Column(
            modifier = Modifier
                .background(DarkerGray)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = note.title.ifBlank { "Pas de titre" },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold, color = Color.White
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White.copy(alpha = 0.1f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = note.body.ifBlank { "Pas de contenu" },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    ),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(2f).horizontalScroll(rememberScrollState())
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = note.dateCreated,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AnimatedVisibility(
                    visible = isBookmarked,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Bookmark,
                        contentDescription = "Favoris",
                        tint = MyRed,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box {
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = FeatherIcons.MoreHorizontal,
                            contentDescription = "Options supplémentaires",
                            tint = IconColorDefault,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    if (expanded) {
                        Box {
                            Popup(
                                alignment = Alignment.TopEnd,
                                onDismissRequest = { expanded = false }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(DarkerGray, RoundedCornerShape(10.dp))
                                        .width(250.dp)
                                        .padding(16.dp)
                                ) {
                                    Column {
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
                                            text = if (isBookmarked) "Retirer" else "Favoris",
                                            icon = FontAwesomeIcons.Regular.Bookmark,
                                            iconTint = Color.White,
                                            onClick = {
                                                isBookmarked = !isBookmarked
                                                expanded = false
                                            })

                                        Divider(
                                            color = Color.White.copy(alpha = 0.1f),
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )

                                        DropdownItem(
                                            text = "Supprimer",
                                            textColor = MyRed,
                                            icon = FeatherIcons.Trash2,
                                            iconTint = MyRed,
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

    if (showDialog) {
        ViewNoteDialog(
            note = note,
            onDismiss = { showDialog = false }
        )
    }
}







