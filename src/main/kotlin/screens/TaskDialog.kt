import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import models.Programmer
import network.apiCreateTask
import network.apiGetAssignedProgrammers

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateTaskDialog(
    projectId: Int,
    onDismiss: () -> Unit,
    onTaskCreated: () -> Unit,
    programmers: List<Programmer>
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var estimacion by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }

    var assignedProgrammers by remember { mutableStateOf<List<Pair<Int, String>>>(emptyList()) }
    var selectedProgrammer by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        apiGetAssignedProgrammers(
            proyectoId = projectId,
            onSuccessResponse = { assignedIds ->
                assignedProgrammers = programmers
                    .filter { it.programador_id in assignedIds }
                    .map { Pair(it.programador_id, it.nombre) }

                if (assignedProgrammers.isNotEmpty()) {
                    selectedProgrammer = assignedProgrammers[0]
                }
            },
            onError = { errorMsg ->
                println("Error cargando programadores asignados: $errorMsg")
            }
        )
    }

    AlertDialog(
        onDismissRequest = { if (!isCreating) onDismiss() },
        title = {
            Text(
                text = "Crear Nueva Tarea",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0A9396))
            )
        },
        text = {
            Column(modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = estimacion,
                    onValueChange = { estimacion = it },
                    label = { Text("Estimación (horas)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Asignar a programador:", fontWeight = FontWeight.Bold)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedProgrammer?.second ?: "Seleccionar",
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
                        assignedProgrammers.forEach { (id, nombre) ->
                            DropdownMenuItem(onClick = {
                                selectedProgrammer = Pair(id, nombre)
                                expanded = false
                            }) {
                                Text(nombre)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && descripcion.isNotBlank() && estimacion.isNotBlank() && selectedProgrammer != null) {
                        isCreating = true
                        apiCreateTask(
                            nombre = nombre,
                            descripcion = descripcion,
                            estimacion = estimacion.toInt(),
                            fechaFinalizacion = null,
                            programadorId = selectedProgrammer!!.first,
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0A9396), contentColor = Color.White)
            ) {
                if (isCreating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Crear", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            Button(
                onClick = { if (!isCreating) onDismiss() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray, contentColor = Color.Black)
            ) {
                Text("Cancelar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White
    )
}