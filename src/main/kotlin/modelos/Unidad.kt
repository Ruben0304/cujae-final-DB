package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Unidad(

    val codigo: String,

    val nombre: String,

    val ubicacion: String
)
