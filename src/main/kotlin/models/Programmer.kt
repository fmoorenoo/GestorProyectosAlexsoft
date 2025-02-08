package models

import kotlinx.serialization.Serializable

@Serializable
data class Programmer(
    val programador_id: Int,
    val sueldo_hora: String,
    val empleado_id: Int,
    val email: String,
    val area: String,
    val clase: String,
    val nivel: String,
    val nombre: String
)
