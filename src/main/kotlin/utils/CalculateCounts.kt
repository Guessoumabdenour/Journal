package utils

import androidx.compose.runtime.Composable
import journal.model.Note
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun calculateCounts(notes: List<Note>): Triple<Int, Int, Int> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    val dateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRENCH)

    notes.forEachIndexed { index, note ->
        println("Note $index:")
        println("  Date Created: ${note.dateCreated}")
        try {
            val parsedDate = dateFormat.parse(note.dateCreated)
            if (parsedDate != null) {
                val noteYear = Calendar.getInstance().apply { time = parsedDate }.get(Calendar.YEAR)
                println("  Parsed Year: $noteYear")
            } else {
                println("  Failed to parse date")
            }
        } catch (e: Exception) {
            println("  Exception parsing date: ${e.message}")
        }
    }

    val entriesThisYear = notes.count { note ->
        println("Attempting to parse note date: ${note.dateCreated}")
        try {
            val parsedDate = dateFormat.parse(note.dateCreated)
            parsedDate?.let {
                val noteYear = Calendar.getInstance().apply { time = it }.get(Calendar.YEAR)
                println("  Parsed Year: $noteYear, Current Year: $currentYear")
                noteYear == currentYear
            } ?: false
        } catch (e: Exception) {
            println("  Exception parsing date: ${e.message}")
            false
        }
    }

    val wordsWritten = notes.sumOf { note ->
        note.body
            .replace("[^\\p{L}\\s]".toRegex(), "")
            .replace("\\s+".toRegex(), " ")
            .trim()
            .split(" ")
            .count { it.isNotBlank() }
    }

    val distinctWritingDays = notes
        .map { note ->
            try {
                val parsedDate = dateFormat.parse(note.dateCreated)
                parsedDate?.let {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
                }
            } catch (e: Exception) {
                null
            }
        }
        .filterNotNull()
        .toSet()
        .size

    println("Entries This Year: $entriesThisYear")
    println("Words Written: $wordsWritten")
    println("Distinct Writing Days: $distinctWritingDays")

    return Triple(entriesThisYear, wordsWritten, distinctWritingDays)
}