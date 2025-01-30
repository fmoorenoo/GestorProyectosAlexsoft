import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

class WelcomeScreen(private val username: String, private val onLogout: () -> Unit) : Screen {
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Welcome, $username!",
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = "Rol: Fucking Boss",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            ),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Text(
                            text = "Proyectos Activos",
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "1. A",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = "2. AA",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = "3. AAA",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            ),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Text(
                            text = "Historial de proyectos:",
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "1. E",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = "2. EE",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
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
