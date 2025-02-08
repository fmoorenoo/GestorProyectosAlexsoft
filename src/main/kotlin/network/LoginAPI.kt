package network

import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.sha512
import io.ktor.client.request.*
import io.ktor.http.ContentType
import models.LoginRequest
import models.User
import network.NetworkUtils.httpClient

fun apiLogIn(
    usuario: String,
    password: String,
    onSuccessResponse: (User) -> Unit,
    onError: (String) -> Unit
) {
    val url = "http://127.0.0.1:5000/login"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val encryptedPassword = sha512(password)
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(usuario, encryptedPassword))
            }

            if (response.status == HttpStatusCode.OK) {
                val user = response.body<User>()
                onSuccessResponse(user)
            } else {
                onError("Usuario o contraseña incorrectos.")
            }
        } catch (e: Exception) {
            onError("Error de conexión. Intenta nuevamente.")
        }
    }
}
