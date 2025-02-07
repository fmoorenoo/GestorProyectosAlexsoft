package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import models.Project
import network.apiHistory

class HistoryScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var finishedProjects by remember { mutableStateOf<List<Project>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            apiHistory(
                onSuccessResponse = { projects ->
                    finishedProjects = projects
                    isLoading = false
                }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Proyectos Acabados") },
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    finishedProjects.isNotEmpty() -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            items(finishedProjects) { project ->
                                FinishedProjectItem(project = project)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    else -> {
                        Text(
                            text = "No hay proyectos acabados",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray,
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FinishedProjectItem(project: Project) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
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
                    text = project.nombre,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF005F73)
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Inicio: ${project.fechaInicio}",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF0A9396)
                )
            )
            Text(
                text = "Finalización: ${project.fechaFinalizacion}",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF0A9396)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = project.descripcion,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                )
            )
        }
    }
}
