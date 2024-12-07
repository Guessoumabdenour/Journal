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
import journal.ui.DarkPurple
import journal.ui.LightPurple
import kotlinx.coroutines.delay
import java.io.File

@Composable
@Preview
fun AppWithTransition() {
    var showSplash by remember { mutableStateOf(true) }
    val viewModel = MyJournalState()

    // Use a Crossfade transition for the smooth switch between Splash and Main UI
    Crossfade(targetState = showSplash, animationSpec = tween(durationMillis = 1200, easing = { it })) { isSplashScreen ->
        if (isSplashScreen) {
            // Splash Screen composable
            SplashScreen(onSplashFinish = { showSplash = false })
        } else {
            // Main App screen
            MaterialTheme {
                Column {
                    App(viewModel)
                }
            }
        }
    }

    // Handle splash screen duration
    LaunchedEffect(Unit) {
        delay(3000)  // Splash screen duration (3 seconds)
        showSplash = false
    }
}

@Composable
fun SplashScreen(onSplashFinish: () -> Unit) {
    // Splash Screen with transparent background and centered logo
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Transparent background, using Color.Transparent
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent) // Transparent background
        )

        // Centered and resized logo
        Image(
            painter = painterResource("icons/logo.png"), // Replace with your logo file
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp) // Adjust size
        )

        // Trigger the finish after delay (3 seconds)
        LaunchedEffect(Unit) {
            delay(3000)
            onSplashFinish() // Transition to main app after splash duration
        }
    }
}

fun main() = application {
    val imageBitmap = loadImageBitmap(File("src/main/resources/icons/logo.png").inputStream())
    val icon = BitmapPainter(imageBitmap)

    // Window setup with transparent background and undecorated
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Journal",
        icon = icon,
        state = windowState,
        transparent = false,  // Ensure the window is not fully transparent for a background color
    ) {
        // Apply a purple background color to the window
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DarkPurple, LightPurple)
                    )
                )// Purple background color
        ) {
            AppWithTransition()
            AppWithTransition()
        }
    }
}
