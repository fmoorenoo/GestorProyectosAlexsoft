package network

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.AssignProgrammerRequest
import network.NetworkUtils.httpClient



fun apiAssignProgrammerToTask(
    tareaId: Int,
    programadorId: Int,
    onSuccessResponse: (String) -> Unit,
    onError: (String) -> Unit
) {
    val url = "http://127.0.0.1:5000/asignar_programador_tarea"
    val requestBody = AssignProgrammerRequest(
        tarea = tareaId,
        programador = programadorId
    )

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            val responseBody = response.bodyAsText()

            if (response.status == HttpStatusCode.OK) {
                onSuccessResponse(responseBody)
            } else {
                onError("Error: ${response.status}, Body: $responseBody")
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        }
    }
}
