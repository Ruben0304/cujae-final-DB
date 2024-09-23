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
data class DoctorId(val medico_id: String)

@Serializable
data class CrearMedicoRequest(
    val p_codigo: String,
    val p_nombre: String,
    val p_apellidos: String,
    val p_especialidad: String,
    val p_numero_licencia: String,
    val p_telefono: String,
    val p_anios_experiencia: Int,
    val p_datos_contacto: String,
    val p_unidad_codigo: String? =null,
    val p_departamento_codigo: String? = null,
    val p_hospital_codigo: String
)

@Serializable
data class UniqueCodeMedico(val generate_unique_medico_code: String)

