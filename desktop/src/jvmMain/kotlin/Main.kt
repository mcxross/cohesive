import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.mcxross.cohesive.common.view.SplashScreen
import com.mcxross.cohesive.common.utils.getPreferredWindowSize
import com.mcxross.cohesive.common.view.MainScreen
import kotlinx.coroutines.delay

fun main() = application {
    var isPerformingTask by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        isPerformingTask = false
    }

    if (isPerformingTask) {

        Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            resizable = false,
            state = WindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                size = getPreferredWindowSize(400, 300),
            ),
        ) {
           SplashScreen()
        }

    } else {

        Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            state = WindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                size = getPreferredWindowSize(800, 1000)
            ),
            icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
        ) {
            MainScreen()
        }
    }
}
