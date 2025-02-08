package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.Programmer
import network.NetworkUtils.httpClient


fun apiGetProgrammers(
    onSuccessResponse: (List<Programmer>) -> Unit,
    onError: (String) -> Unit
) {
    val url = "http://127.0.0.1:5000/empleado/programadores"

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.get(url)
            val responseBody = response.bodyAsText()


            if (response.status == HttpStatusCode.OK) {
                val programmers = response.body<List<Programmer>>()
                onSuccessResponse(programmers)
            } else {
                onError("Error: ${response.status}, Body: $responseBody")
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        }
    }
}