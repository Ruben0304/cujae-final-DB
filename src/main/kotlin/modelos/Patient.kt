package modelos

import kotlinx.datetime.LocalDate
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
    @SerialName("ci") val numeroHistoriaClinica: String,
    @SerialName("nombre") val nombre: String,
    @SerialName("apellidos") val apellidos: String,
    @SerialName("fecha_nacimiento") val fechaNacimiento: String, // Puede ser String o LocalDate
    @SerialName("direccion") val direccion: String,
)

@Serializable
data class PatientRequest(
    val ci: String,
    val nombre: String,
    val apellidos: String,
    val fecha_nacimiento: LocalDate,
    val direccion: String
)

@Serializable
data class PacienteParaComprobar(
    @SerialName("ci") val ci: String,
)

@Serializable
data class Registro(
    val registro_id : Int,
    val ci: String,
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

