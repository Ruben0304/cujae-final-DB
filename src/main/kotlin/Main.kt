import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import vistas.componentes.MacOSTitleBar
import vistas.login.DefaultPreview
import vistas.login.LoginScreen
import vistas.login.StartLoading

fun main() = application {
    val windowState = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        size = DpSize(900.dp, 700.dp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Proyecto final BD PostgreSQL",
        undecorated = true
    ) {
        Column {
            MacOSTitleBar(windowState)
            StartLoading()
        }
    }
}
