package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Doctor(
    @SerialName("codigo") val codigo: String,
    @SerialName("nombre") val nombre: String,
    @SerialName("unidad") val unidad: String,
    @SerialName("departamento") val departamento: String,
    @SerialName("hospital") val hospital: String,
    @SerialName("apellidos") val apellidos: String,
    @SerialName("especialidad") val especialidad: String,
    @SerialName("numero_licencia") val numeroLicencia: String,
    @SerialName("telefono") val telefono: String,
    @SerialName("anios_experiencia") val aniosExperiencia: Int,
    @SerialName("datos_contacto") val datosContacto: String
)

@Serializable
data class DoctorId(val medico_id : String)