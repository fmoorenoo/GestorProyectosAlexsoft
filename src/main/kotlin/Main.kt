import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.unit.DpSize
import cafe.adriel.voyager.navigator.Navigator
import screens.LoginScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Gestor de Proyectos",
        state = WindowState(size = DpSize(800.dp, 800.dp))
    ) {
        Navigator(LoginScreen())
    }
}
