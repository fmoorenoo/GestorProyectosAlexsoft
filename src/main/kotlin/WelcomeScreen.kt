import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.launch

data class Project(val name: String, val startDate: String, val description: String)

class WelcomeScreen(
    private val username: String,
    private val onLogout: () -> Unit,
    private val onViewProjects: () -> Unit
) : Screen {
    private val activeProjects = listOf(
        Project("Proyecto A", "01/01/2024", "Descripción A..."),
        Project("Proyecto B", "15/02/2024", "Descripción B..."),
        Project("Proyecto C", "10/03/2024", "Descripción C..."),
        Project("Proyecto D", "05/04/2024", "Descripción D..."),
        Project("Proyecto E", "20/05/2024", "Descripción E...")
    )

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF518c79), Color(0xFF9fe1c6))
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF5F5F5),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Welcome, $username!",
                            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = "Rol: Fucking Boss",
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Proyectos Activos",
                                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = Color.Black),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = onViewProjects) {
                                Icon(
                                    imageVector = Icons.Filled.Visibility,
                                    contentDescription = "Ver proyectos",
                                    tint = Color(0xFF00796B)
                                )
                            }
                        }

                        val scrollState = rememberLazyListState()
                        val coroutineScope = rememberCoroutineScope()
                        LazyRow(
                            state = scrollState,
                            modifier = Modifier
                                .draggable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberDraggableState { delta ->
                                        coroutineScope.launch {
                                            scrollState.scrollBy(-delta)
                                        }
                                    },
                                )
                        ) {
                            items(activeProjects) { project ->
                                ProjectCard(project)
                            }
                        }
                    }

                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB00020))
                    ) {
                        Text(
                            text = "Desconectar",
                            style = TextStyle(fontSize = 18.sp, color = Color.White)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(150.dp)
            .width(190.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = project.name,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Inicio: ${project.startDate}",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray),
                textAlign = TextAlign.Center
            )
            Text(
                text = project.description,
                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                textAlign = TextAlign.Center
            )
        }
    }
}
