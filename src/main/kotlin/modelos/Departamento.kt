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
data class ResumenProcesoResult(
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

