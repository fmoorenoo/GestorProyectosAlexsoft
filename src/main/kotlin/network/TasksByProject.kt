package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.NetworkUtils.httpClient
import models.Task



fun apiGetTasksByProject(projectId: Int, onSuccessResponse: (List<Task>) -> Unit, onError: (String) -> Unit) {
    val url = "http://127.0.0.1:5000/tareas?id=$projectId"

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.get(url)
            val responseBody = response.bodyAsText()

            if (response.status == HttpStatusCode.OK) {
                val tasks = response.body<List<Task>>()
                onSuccessResponse(tasks)
            } else {
                onError("Error: ${response.status}, Body: $responseBody")
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        }
    }
}
