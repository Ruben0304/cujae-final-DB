package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hospital(
    @SerialName("hospital_nombre") val nombre: String,
    @SerialName("cantidad_departamentos") val cantidadDepartamentos: Int,
    @SerialName("cantidad_unidades") val cantidadUnidades: Int,
    @SerialName("cantidad_medicos") val cantidadMedicos: Int,
    @SerialName("cantidad_pacientes") val cantidadPacientes: Int
)

@Serializable
data class HospitalNombres(
     val codigo: String,
     val nombre: String,

)

