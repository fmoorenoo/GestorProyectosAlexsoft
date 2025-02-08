package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.NetworkUtils.httpClient

fun apiAssignProgrammerToProject(
    programadorId: Int,
    proyectoId: Int,
    onSuccessResponse: (String) -> Unit,
    onError: (String) -> Unit
) {
    val url = "http://127.0.0.1:5000/asignar_programador"

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(mapOf("programador" to programadorId, "proyecto" to proyectoId))
            }

            val responseBody = response.bodyAsText()

            if (response.status == HttpStatusCode.Created) {
                onSuccessResponse("Programador $programadorId asignado correctamente.")
            } else {
                onError("Error: ${response.status}, $responseBody")
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        }
    }
}
