import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

data class Project(
    val id: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val description: String
)

class ProjectsScreen : Screen {
    private val activeProjects = listOf(
        Project(1, "Proyecto A", "01/01/2024", "31/12/2024", "Descripción del Proyecto A..."),
        Project(2, "Proyecto B", "15/02/2024", "30/11/2024", "Descripción del Proyecto B..."),
        Project(3, "Proyecto C", "10/03/2024", "15/10/2024", "Descripción del Proyecto C..."),
        Project(4, "Proyecto D", "05/04/2024", "20/09/2024", "Descripción del Proyecto D..."),
        Project(5, "Proyecto E", "20/05/2024", "10/08/2024", "Descripción del Proyecto E...")
    )

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Proyectos Activos") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    backgroundColor = Color(0xFF0A9396),
                    contentColor = Color.White
                )
            },
            backgroundColor = Color(0xFFF0F0F0)
        ) {

        }
    }
}