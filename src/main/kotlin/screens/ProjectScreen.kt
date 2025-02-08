package screens

import CreateTaskDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
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
import kotlinx.coroutines.launch
import models.Project
import models.Task
import models.Programmer
import network.apiGetTasksByProject
import network.apiGetProgrammers

class ProjectScreen(private val project: Project) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
        var programmers by remember { mutableStateOf<List<Programmer>>(emptyList()) }
        var isLoadingTasks by remember { mutableStateOf(true) }
        var isLoadingProgrammers by remember { mutableStateOf(true) }
        var showDialog by remember { mutableStateOf(false) }
        var recomposeTrigger by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(project.id, recomposeTrigger) {
            isLoadingTasks = true
            apiGetTasksByProject(
                projectId = project.id,
                onSuccessResponse = { fetchedTasks ->
                    tasks = fetchedTasks
                    isLoadingTasks = false
                },
                onError = {
                    isLoadingTasks = false
                }
            )
        }

        LaunchedEffect(Unit) {
            isLoadingProgrammers = true
            apiGetProgrammers(
                onSuccessResponse = { fetchedProgrammers ->
                    programmers = fetchedProgrammers
                    isLoadingProgrammers = false
                },
                onError = {
                    isLoadingProgrammers = false
                }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = project.nombre) },
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
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tareas del Proyecto",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005F73)),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (isLoadingTasks) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (tasks.isEmpty()) {
                    Text(
                        text = "No hay tareas asignadas",
                        style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    ) {
                        items(tasks) { task ->
                            TaskItem(task = task, navigator = navigator)
                        }
                    }
                }

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0A9396))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir tarea", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Añadir Tarea", style = TextStyle(fontSize = 18.sp, color = Color.White))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Asignar Programadores",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005F73)),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (isLoadingProgrammers) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (programmers.isEmpty()) {
                    Text(
                        text = "No hay programadores disponibles",
                        style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    ) {
                        items(programmers) { programmer ->
                            ProgrammerItem(programmer = programmer)
                        }
                    }
                }
            }
        }

        if (showDialog) {
            CreateTaskDialog(
                projectId = project.id,
                onDismiss = { showDialog = false },
                onTaskCreated = {
                    showDialog = false
                    recomposeTrigger = !recomposeTrigger
                }
            )
        }
    }
}

@Composable
fun TaskItem(task: Task, navigator: cafe.adriel.voyager.navigator.Navigator) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { navigator.push(TasksScreen(task)) },
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.nombre,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005F73))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.descripcion,
                style = TextStyle(fontSize = 14.sp, color = Color.Black)
            )
        }
    }
}

@Composable
fun ProgrammerItem(programmer: Programmer) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.Person, contentDescription = "Programador", tint = Color(0xFF0A9396))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = programmer.nombre,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                )
                Text(
                    text = "Email: ${programmer.email}",
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                )
                Text(
                    text = "Área: ${programmer.area}",
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                )
            }
        }
    }
}