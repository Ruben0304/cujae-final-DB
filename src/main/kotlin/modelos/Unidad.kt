package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Unidad(

    val codigo: String,
    @SerialName("departamento_codigo") val departamento: String,
    val nombre: String,
    val ubicacion: String
)
