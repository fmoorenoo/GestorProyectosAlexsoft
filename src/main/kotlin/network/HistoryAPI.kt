package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.Project
import network.NetworkUtils.httpClient

fun apiHistory(onSuccessResponse: (List<Project>) -> Unit) {
    val url = "http://127.0.0.1:5000/proyecto/proyectos_acabados"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.get(url)
            val responseBody = response.bodyAsText()
            if (response.status == HttpStatusCode.OK) {
                val projects = response.body<List<Project>>()
                onSuccessResponse(projects)
            } else {
                println("Error: ${response.status}, Body: $responseBody")
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
    }
}
