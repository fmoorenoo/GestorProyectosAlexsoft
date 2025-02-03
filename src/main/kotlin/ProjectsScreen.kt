import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

data class Project(
    val id: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    var isMine: Boolean
)

class ProjectsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var showOnlyMine by remember { mutableStateOf(false) } // Estado para el filtro
        val activeProjects = remember {
            mutableStateListOf(
                Project(1, "Proyecto A", "01/01/2024", "31/12/2024", "Descripción del Proyecto A...", true),
                Project(2, "Proyecto B", "15/02/2024", "30/11/2024", "Descripción del Proyecto B...", false),
                Project(3, "Proyecto C", "10/03/2024", "15/10/2024", "Descripción del Proyecto C...", true),
                Project(4, "Proyecto D", "05/04/2024", "20/09/2024", "Descripción del Proyecto D...", false),
                Project(5, "Proyecto E", "20/05/2024", "10/08/2024", "Descripción del Proyecto E...", true)
            )
        }

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
                    contentColor = Color.White,
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Text(
                                text = "Solo míos",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Switch(
                                checked = showOnlyMine,
                                onCheckedChange = { showOnlyMine = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFFf1a56a),
                                    checkedTrackColor = Color(0xFFf4bc90),
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color(0xFFeeeeee)
                                )
                            )
                        }
                    }
                )
            },
            backgroundColor = Color(0xFFF0F0F0)
        ) { paddingValues ->
            val filteredProjects = if (showOnlyMine) {
                activeProjects.filter { it.isMine }
            } else {
                activeProjects
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(filteredProjects) { project ->
                    projectItem(
                        project = project,
                        onClick = { navigator.push(ProjectScreen(project)) },
                        onViewMine = {
                            val index = activeProjects.indexOfFirst { it.id == project.id }
                            if (index != -1) {
                                activeProjects[index] = activeProjects[index].copy(isMine = !activeProjects[index].isMine)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun projectItem(project: Project, onClick: () -> Unit, onViewMine: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.name,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF005F73)
                    )
                )
                IconButton(
                    onClick = { onViewMine() },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = if (project.isMine) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "Añadir a mis proyectos",
                        tint = if (project.isMine) Color(0xFFf1a56a) else Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Inicio: ${project.startDate}",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF0A9396)
                )
            )
            Text(
                text = "Finalización: ${project.endDate}",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF0A9396)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = project.description,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                )
            )
        }
    }
}