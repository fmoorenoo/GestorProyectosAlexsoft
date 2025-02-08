package models

import kotlinx.serialization.Serializable

@Serializable
data class AssignProgrammerRequest(
    val tarea: Int,
    val programador: Int
)