package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
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
import network.apiActiveProjects
import network.apiProjectsByGestor

class ProjectsScreen(private val gestorId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var showOnlyMine by remember { mutableStateOf(false) }
        var myProjectsIds by remember { mutableStateOf<List<Int>>(emptyList()) }

        LaunchedEffect(showOnlyMine) {
            isLoading = true

            if (showOnlyMine) {
                apiProjectsByGestor(
                    idGestor = gestorId,
                    onSuccessResponse = { gestorProjects ->
                        myProjectsIds = gestorProjects.map { it.id }
                        projects = gestorProjects
                        isLoading = false
                    }
                )
            } else {
                apiActiveProjects(
                    onSuccessResponse = { activeProjects ->
                        projects = activeProjects
                        isLoading = false
                    }
                )
            }
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    projects.isNotEmpty() -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            items(projects) { project ->
                                val isMine = myProjectsIds.contains(project.id)
                                projectItem(
                                    project = project,
                                    isMine = isMine,
                                    navigator = navigator
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                    else -> {
                        Text(
                            text = "No hay proyectos disponibles",
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
fun projectItem(project: Project, isMine: Boolean, navigator: cafe.adriel.voyager.navigator.Navigator) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigator.push(ProjectScreen(project)) },
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
                Icon(
                    imageVector = if (isMine) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                    contentDescription = if (isMine) "Proyecto propio" else "Proyecto no propio",
                    tint = if (isMine) Color(0xFFf1a56a) else Color.Gray
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
