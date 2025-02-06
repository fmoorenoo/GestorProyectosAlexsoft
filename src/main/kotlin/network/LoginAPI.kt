package network

import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.http.cio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.sha512
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import models.LoginRequest
import models.User
import network.NetworkUtils.httpClient



fun apiLogIn(usuario: String, password: String, onSuccessResponse: (User) -> Unit) {
    val url = "http://127.0.0.1:5000/gestor/login"
    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(usuario, sha512(password)))
        }
        if (response.status == HttpStatusCode.OK) {
            val user = response.body<User>()
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}