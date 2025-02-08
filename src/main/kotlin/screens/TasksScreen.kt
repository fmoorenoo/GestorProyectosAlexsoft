package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import models.Programmer
import models.Task
import network.apiAssignProgrammerToTask
import network.apiGetAssignedProgrammers

class TasksScreen(private val task: Task, private val programmers: List<Programmer>) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var assignedProgrammers by remember { mutableStateOf<List<Int>>(emptyList()) }
        var selectedProgrammer by remember { mutableStateOf<Programmer?>(null) }
        var showDialog by remember { mutableStateOf(false) }
        var isAssigning by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            apiGetAssignedProgrammers(
                proyectoId = task.proyecto,
                onSuccessResponse = { assignedIds ->
                    assignedProgrammers = assignedIds
                    selectedProgrammer = programmers.find { it.programador_id == task.programador }
                },
                onError = { println("Error al obtener programadores asignados") }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalles de la Tarea") },
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp,
                    backgroundColor = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Nombre: ${task.nombre}",
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005F73))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Descripción: ${task.descripcion}",
                            style = TextStyle(fontSize = 16.sp, color = Color.Black)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Programador actual: ${selectedProgrammer?.nombre ?: "Desconocido"}",
                            style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0A9396), contentColor = Color.White)
                ) {
                    Text("Asignar nuevo programador", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showDialog) {
            AssignProgrammerDialog(
                assignedProgrammers = assignedProgrammers,
                programmers = programmers,
                onDismiss = { showDialog = false },
                onAssign = { newProgrammer ->
                    isAssigning = true
                    apiAssignProgrammerToTask(
                        tareaId = task.id,
                        programadorId = newProgrammer.programador_id,
                        onSuccessResponse = {
                            isAssigning = false
                            selectedProgrammer = newProgrammer
                            showDialog = false
                        },
                        onError = { errorMsg ->
                            isAssigning = false
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AssignProgrammerDialog(
    assignedProgrammers: List<Int>,
    programmers: List<Programmer>,
    onDismiss: () -> Unit,
    onAssign: (Programmer) -> Unit
) {
    var selectedProgrammer by remember { mutableStateOf<Programmer?>(null) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Asignar Programador", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Selecciona un programador para asignar a esta tarea:", fontSize = 14.sp)

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedProgrammer?.nombre ?: "Seleccionar",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expandir")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        programmers.filter { it.programador_id in assignedProgrammers }.forEach { programmer ->
                            DropdownMenuItem(onClick = {
                                selectedProgrammer = programmer
                                expanded = false
                            }) {
                                Text(programmer.nombre)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedProgrammer != null) {
                        onAssign(selectedProgrammer!!)
                    }
                },
                enabled = selectedProgrammer != null,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0A9396), contentColor = Color.White)
            ) {
                Text("Asignar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray, contentColor = Color.Black)
            ) {
                Text("Cancelar")
            }
        }
    )
}
