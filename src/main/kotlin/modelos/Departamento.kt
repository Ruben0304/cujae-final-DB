package modelos

import kotlinx.serialization.Serializable

@Serializable
data class Departamento(
    val departamento_codigo: String,
    val departamento_nombre: String,
)

