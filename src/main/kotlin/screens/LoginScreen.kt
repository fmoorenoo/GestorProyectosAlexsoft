package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import models.User
import network.apiLogIn

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var user: User by remember { mutableStateOf(User(0, 0, "", "")) }
        var passwordVisible by remember { mutableStateOf(false) }
        val navigator = LocalNavigator.current

        val darkblue = 0xFF518c79
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Color(darkblue), Color(0xFF9fe1c6))))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .width(450.dp)
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Alexsoft  -  Iniciar Sesión",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "User Icon",
                                    tint = Color(darkblue),
                                    modifier = Modifier.size(30.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text="Usuario")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(darkblue),
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            cursorColor = Color(darkblue),
                            focusedLabelColor = Color(darkblue)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Passwd Icon",
                                    tint = Color(darkblue),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text="Contraseña")
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(darkblue),
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            cursorColor = Color(darkblue),
                            focusedLabelColor = Color(darkblue)
                        ),
                        trailingIcon = {
                            if (password.isNotEmpty()) {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = "",
                                        tint = Color(0xFF477869)
                                    )
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            apiLogIn(username, password){
                                user = it
                                if(!user.nombre.isEmpty()){
                                    navigator?.push(WelcomeScreen(username = username, onLogout = { navigator.pop() }, onViewProjects = { navigator.push(ProjectsScreen()) }, onViewHistory = { navigator.pop()}))
                                }
                            }
                        },
                        enabled = username.isNotEmpty() && password.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF648f81))
                    ) {
                        Text(
                            text = "Iniciar Sesión",
                            style = TextStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
