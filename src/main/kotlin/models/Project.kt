package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName("id") val id: Int,
    @SerialName("nombre") val nombre: String,
    @SerialName("descripcion") val descripcion: String,
    @SerialName("fecha_creacion") val fechaCreacion: String,
    @SerialName("fecha_inicio") val fechaInicio: String,
    @SerialName("fecha_finalizacion") val fechaFinalizacion: String,
    @SerialName("cliente") val cliente: Int
)
