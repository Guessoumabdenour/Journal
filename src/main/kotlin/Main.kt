import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import journal.model.MyJournalState
import androidx.compose.foundation.layout.Column
import journal.ui.App
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import journal.ui.theme.*
import kotlinx.coroutines.delay
import java.io.File

@Composable
@Preview
fun AppWithTransition() {
    var showSplash by remember { mutableStateOf(true) }
    val viewModel = MyJournalState()

    Crossfade(targetState = showSplash, animationSpec = tween(durationMillis = 1200, easing = { it })) { isSplashScreen ->
        if (isSplashScreen) {
            SplashScreen(onSplashFinish = { showSplash = false })
        } else {
            MaterialTheme {
                Column {
                    App(viewModel)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false
    }
}

@Composable
fun SplashScreen(onSplashFinish: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        )
        Image(
            painter = painterResource("icons/logo.png"),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )

        LaunchedEffect(Unit) {
            delay(3000)
            onSplashFinish()
        }
    }
}

fun main() = application {
    val imageBitmap = loadImageBitmap(File("src/main/resources/icons/logo.png").inputStream())
    val icon = BitmapPainter(imageBitmap)

    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Journal",
        icon = icon,
        state = windowState,
        transparent = false,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DarkPurple, LightPurple)
                    )
                )
        ) {
            AppWithTransition()
            AppWithTransition()
        }
    }
}
