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

@Serializable
data class UResumenProcesoParam(
    @SerialName("p_unidad_codigo") val unidadCodigo: String,
    @SerialName("p_departamento_codigo") val departamentoCodigo: String,
    @SerialName("p_hospital_codigo") val hospitalCodigo: String,
)
@Serializable
data class UResumenProcesoResult(
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
data class URevisarTurnosParam(
    @SerialName("p_hospital_codigo") val hospital: String,
    @SerialName("p_departamento_codigo") val departamento: String
)

@Serializable
data class URevisarTurnosResult(
    val hospital_nombre: String,
    val departamento_nombre: String,
    val unidad_nombre: String,
    val turno_numero: Int,
    val total_pacientes: Int,
    val medico_nombre: String,
    val medico_apellidos: String,
    val cantidad_pacientes_atendidos: Int,
    val porcentaje_consultas: Double
)



@Serializable
data class UResumenConsultasExitosasParam(
    @SerialName("p_unidad_codigo") val unidad: String,
    @SerialName("p_departamento_codigo") val departamento: String,
    @SerialName("p_hospital_codigo") val hospital: String

)


@Serializable
data class UResumenConsultasExitosasResult(
    val hospital_nombre: String,
    val departamento_nombre: String,
    val unidad_nombre: String,
    val turno_numero: Int,
    val medico_nombre: String,
    val medico_apellidos: String,
    val cantidad_pacientes_atendidos: Int,
    val pacientes_atendidos_por_turno: Int
)


@Serializable
data class UPacientesNoAtendParam(
    @SerialName("p_unidad_codigo") val unidad: String,
    @SerialName("p_departamento_codigo") val departamento: String,
    @SerialName("p_hospital_codigo") val hospital: String

)


@Serializable
data class UPacientesNoAtendResult(
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




