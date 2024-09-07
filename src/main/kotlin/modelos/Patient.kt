package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Patient(
    @SerialName("numero_historia_clinica") val numeroHistoriaClinica: String,
    @SerialName("paciente_nombre") val nombre: String,
    @SerialName("paciente_apellidos") val apellidos: String,
    @SerialName("estado_atencion") val estadoAtencion: String,
    @SerialName("fecha_atencion") val fechaAtencion: String // Puede ser String o LocalDateTime
)



@Serializable
data class PatientTable(
    @SerialName("numero_historia_clinica") val numeroHistoriaClinica: String,
    @SerialName("nombre") val nombre: String,
    @SerialName("apellidos") val apellidos: String,
    @SerialName("fecha_nacimiento") val fechaNacimiento: String, // Puede ser String o LocalDate
    @SerialName("direccion") val direccion: String,
    @SerialName("unidad_codigo") val unidadCodigo: String,
    @SerialName("departamento_codigo") val departamentoCodigo: String,
    @SerialName("hospital_codigo") val hospitalCodigo: String
)

@Serializable
data class Registro(
    val registro_id : Int,
    val numero_historia_clinica: String,
    val nombre: String,
    val apellidos: String,
    val fecha_nacimiento: String, // Puedes usar `LocalDate` si prefieres trabajar con fechas
    val direccion: String,
    val unidad_codigo: String,
    val departamento_codigo: String,
    val hospital_codigo: String,
    val estado: String,
    val causa_no_atencion: String?
)

