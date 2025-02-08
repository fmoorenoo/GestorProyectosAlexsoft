package screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import network.apiCreateTask
import network.apiGetTasksByProject

class ProjectScreen(private val project: Project) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var showDialog by remember { mutableStateOf(false) }
        var recomposeTrigger by remember { mutableStateOf(false) }

        val scrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(project.id, recomposeTrigger) {
            isLoading = true
            apiGetTasksByProject(
                projectId = project.id,
                onSuccessResponse = { fetchedTasks ->
                    tasks = fetchedTasks
                    isLoading = false
                },
                onError = {
                    isLoading = false
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

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (tasks.isEmpty()) {
                    Text(
                        text = "No hay tareas asignadas",
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
                        items(tasks) { task ->
                            TaskItem(task = task)
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
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier.width(220.dp),
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Programador ID: ${task.programador}",
                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
            )
        }
    }
}

@Composable
fun CreateTaskDialog(projectId: Int, onDismiss: () -> Unit, onTaskCreated: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var estimacion by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { if (!isCreating) onDismiss() },
        title = {
            Text(
                text = "Crear Nueva Tarea",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A9396)
                ),
                modifier = Modifier.height(800.dp)
            )
        },

        text = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0A9396),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF0A9396),
                        focusedLabelColor = Color(0xFF0A9396),
                        unfocusedLabelColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0A9396),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF0A9396),
                        focusedLabelColor = Color(0xFF0A9396),
                        unfocusedLabelColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = estimacion,
                    onValueChange = { estimacion = it },
                    label = { Text("Estimación (horas)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0A9396),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF0A9396),
                        focusedLabelColor = Color(0xFF0A9396),
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && descripcion.isNotBlank() && estimacion.isNotBlank()) {
                        isCreating = true
                        apiCreateTask(
                            nombre = nombre,
                            descripcion = descripcion,
                            estimacion = estimacion.toInt(),
                            fechaFinalizacion = null,
                            programadorId = 1,
                            proyectoId = projectId,
                            onSuccessResponse = {
                                isCreating = false
                                onTaskCreated()
                            },
                            onError = {
                                isCreating = false
                            }
                        )
                    }
                },
                enabled = !isCreating,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0A9396),
                    contentColor = Color.White
                )
            ) {
                if (isCreating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(
                        text = "Crear",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        },
        dismissButton = {
            Button(
                onClick = { if (!isCreating) onDismiss() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Cancelar",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        contentColor = Color.Black
    )
}