package screens

import CreateTaskDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import network.apiAssignProgrammerToProject
import network.apiGetAssignedProgrammers

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
        var assignedProgrammers by remember { mutableStateOf<List<Int>>(emptyList()) }

        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberLazyListState()

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
                onSuccessResponse = { programmers = it; isLoadingProgrammers = false },
                onError = { isLoadingProgrammers = false }
            )

            apiGetAssignedProgrammers(
                proyectoId = project.id,
                onSuccessResponse = { assignedProgrammers = it },
                onError = {}
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
                ProjectDetails(project)

                Spacer(modifier = Modifier.height(16.dp))

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
                            TaskItem(task = task, navigator = navigator, programmers = programmers)
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
                    LazyRow(
                        state = scrollState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .draggable(
                                orientation = Orientation.Horizontal,
                                state = rememberDraggableState { delta ->
                                    coroutineScope.launch {
                                        scrollState.scrollBy(-delta)
                                    }
                                }
                            ),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(programmers) { programmer ->
                            val isAssigned = assignedProgrammers.contains(programmer.programador_id)
                            ProgrammerItem(programmer, isAssigned) {
                                coroutineScope.launch {
                                    apiAssignProgrammerToProject(
                                        programadorId = programmer.programador_id,
                                        proyectoId = project.id,
                                        onSuccessResponse = {
                                            assignedProgrammers = assignedProgrammers + programmer.programador_id
                                        },
                                        onError = {}
                                    )
                                }
                            }
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
                },
                programmers = programmers
            )
        }
    }
}


@Composable
fun ProjectDetails(project: Project) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Detalles del Proyecto",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005F73))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Nombre: ${project.nombre}", style = TextStyle(fontSize = 16.sp, color = Color.Black))
            Text(text = "Descripción: ${project.descripcion}", style = TextStyle(fontSize = 14.sp, color = Color.Gray))
            Text(text = "Fecha de Creación: ${project.fechaCreacion}", style = TextStyle(fontSize = 14.sp, color = Color.Gray))
            Text(text = "Fecha de Finalización: ${project.fechaFinalizacion}", style = TextStyle(fontSize = 14.sp, color = Color.Gray))
        }
    }
}


@Composable
fun TaskItem(task: Task, navigator: cafe.adriel.voyager.navigator.Navigator, programmers: List<Programmer>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navigator.push(TasksScreen(task, programmers)) },
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.nombre,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005F73))
            )
        }
    }
}


@Composable
fun ProgrammerItem(programmer: Programmer, isAssigned: Boolean, onAssignClick: () -> Unit) {
    Card(
        modifier = Modifier.width(250.dp).padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        backgroundColor = if (!isAssigned) Color.White  else Color(0xffdaf8ff)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Programador",
                tint = Color(0xFF0A9396)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = programmer.nombre,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = programmer.email,
                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!isAssigned) {
                Button(
                    onClick = onAssignClick,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF0A9396),
                        contentColor = Color.White
                    )
                ) {
                    Text("Asignar")
                }
            } else {
                Text(
                    text = "Asignado",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF0A9396),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

