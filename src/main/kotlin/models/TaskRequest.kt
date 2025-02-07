package models

import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest(
    val nombre: String,
    val descripcion: String,
    val estimacion: Int,
    val fecha_finalizacion: String?,
    val programador: Int,
    val proyecto: Int
)