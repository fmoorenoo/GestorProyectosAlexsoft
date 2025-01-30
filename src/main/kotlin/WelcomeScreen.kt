import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

class WelcomeScreen(private val username: String, private val onLogout: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF5F5F5),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Welcome, $username!",
                    style = MaterialTheme.typography.h4.copy(fontSize = 32.sp, color = Color.Black),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { /* Logic to greet the user */ },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                ) {
                    Text(
                        text = "Saludar al usuario",
                        style = TextStyle(fontSize = 18.sp, color = Color.White)
                    )
                }

                Button(
                    onClick = { /* Show role logic */ },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                ) {
                    Text(
                        text = "Mostrar el rol (Gestor)",
                        style = TextStyle(fontSize = 18.sp, color = Color.White)
                    )
                }

                Button(
                    onClick = { /* Navigate to active projects */ },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                ) {
                    Text(
                        text = "Ver proyectos (activos)",
                        style = TextStyle(fontSize = 18.sp, color = Color.White)
                    )
                }

                Button(
                    onClick = { /* Navigate to project history */ },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
                ) {
                    Text(
                        text = "Historial (proyectos terminados)",
                        style = TextStyle(fontSize = 18.sp, color = Color.White)
                    )
                }

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
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