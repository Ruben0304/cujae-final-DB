package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Departamento(
    val departamento_codigo: String,
    val departamento_nombre: String,
)

@Serializable
data class DResumenProcesoParam(
    @SerialName("p_departamento_codigo") val departamentoCodigo: String,
    @SerialName("p_hospital_codigo") val hospitalCodigo: String,
)

@Serializable
data class DResumenProcesoResult(
    val hospital_nombre: String,
    val departamento_nombre: String,
    val unidad_nombre: String,
    val numero_turno: Int,
    val pacientes_atendidos: Long,
    val total_pacientes: Long,
    val porcentaje_atendidos: Double,
    val pacientes_no_atendidos: Long,
    val pacientes_dados_alta: Long,
    val pacientes_no_atendidos_extranjero: Long,
    val pacientes_no_atendidos_fuera_provincia: Long,
    val pacientes_no_atendidos_hospitalizados: Long,
    val pacientes_no_atendidos_otros: Long,
    val pacientes_no_atendidos_desconocidos: Long
)

@Serializable
data class DPacientesNoAtendParam(
    @SerialName("p_departamento_codigo") val departamento: String,
    @SerialName("p_hospital_codigo") val hospital: String

)


@Serializable
data class DPacientesNoAtendResult(
    @SerialName("hospital_nombre") val hospital: String,
    @SerialName("departamento_nombre") val departamento: String,
    @SerialName("unidad_nombre") val unidad: String,
    @SerialName("numero_turno") val numTurno: Int,
    @SerialName("total_pacientes_no_atendidos") val totalPacNoAtend: Int,
    @SerialName("numero_historia_clinica") val numHistClinica: String,
    @SerialName("paciente_nombre") val nombPac: String,
    @SerialName("paciente_apellidos") val apellPac: String,
    val direccion: String,
    val causa: String
)