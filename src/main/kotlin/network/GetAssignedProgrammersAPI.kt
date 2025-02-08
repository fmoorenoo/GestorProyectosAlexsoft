package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.NetworkUtils.httpClient

fun apiGetAssignedProgrammers(
    proyectoId: Int,
    onSuccessResponse: (List<Int>) -> Unit,
    onError: (String) -> Unit
) {
    val url = "http://127.0.0.1:5000/programadores_asignados/$proyectoId"

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response: HttpResponse = httpClient.get(url)
            val responseBody = response.bodyAsText()

            if (response.status == HttpStatusCode.OK) {
                try {
                    val assignedIds = response.body<List<Int>>()
                    onSuccessResponse(assignedIds)
                } catch (e: Exception) {
                    onError("Error al deserializar la respuesta: ${e.message}")
                }
            } else {
                onError("Error: ${response.status}, Body: $responseBody")
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        }
    }
}
