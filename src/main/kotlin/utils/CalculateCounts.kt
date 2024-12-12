package utils

import androidx.compose.runtime.Composable
import journal.model.Note
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun calculateCounts(notes: List<Note>): Triple<Int, Int, Int> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val entriesThisYear = notes.count {
        val noteDate = SimpleDateFormat("EEEE d MMMM", Locale.FRENCH).parse(it.dateCreated)
        val noteYear = Calendar.getInstance().apply { time = noteDate }.get(Calendar.YEAR)
        noteYear == currentYear
    }

    val wordsWritten = notes.sumOf { note ->
        val cleanedBody = note.body
            .replace("[^\\p{L}\\s]".toRegex(), "")
            .replace("\\s+".toRegex(), " ")
            .trim()

        cleanedBody.split(" ").size
    }

    val distinctWritingDays = notes.map { note ->
        val noteDate = SimpleDateFormat("EEEE d MMMM", Locale.FRENCH).parse(note.dateCreated)
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(noteDate)
    }.toSet().size

    return Triple(entriesThisYear, wordsWritten, distinctWritingDays)
}