import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import journal.model.MyJournalState
import java.io.File
import androidx.compose.foundation.layout.Column
import journal.ui.App

fun main() = application {
    val viewModel = MyJournalState()

    val windowState = rememberWindowState(
        placement = WindowPlacement.Maximized
    )

    val imageBitmap = loadImageBitmap(File("src/main/resources/icons/logo.png").inputStream())
    val icon = BitmapPainter(imageBitmap)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Journal",
        icon = icon,
        state = windowState,
        undecorated = false
    ) {
        Column {
            App(viewModel)
        }
    }
}
