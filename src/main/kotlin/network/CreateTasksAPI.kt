package network

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.NetworkUtils.httpClient
import models.TaskRequest


fun apiCreateTask(
    nombre: String,
    descripcion: String,
    estimacion: Int,
    fechaFinalizacion: String?,
    programadorId: Int,
    proyectoId: Int,
    onSuccessResponse: (String) -> Unit,
    onError: (String) -> Unit
) {
    val url = "http://127.0.0.1:5000/crear_tareas"
    val requestBody = TaskRequest(
        nombre = nombre,
        descripcion = descripcion,
        estimacion = estimacion,
        fecha_finalizacion = fechaFinalizacion,
        programador = programadorId,
        proyecto = proyectoId
    )

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            val responseBody = response.bodyAsText()

            if (response.status == HttpStatusCode.Created) {
                onSuccessResponse(responseBody)
            } else {
                onError("Error: ${response.status}, Body: $responseBody")
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        }
    }
}
