package modelos

import kotlinx.serialization.Serializable

@Serializable
data class Hospital(
    val codigo: String,
    val nombre: String
)