package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Consulta(
    @SerialName("consulta_id") val consultaId: Int,
    @SerialName("turno_numero") val turnoNumero: Int,
    @SerialName("turno_unidad_codigo") val turnoUnidadCodigo: String,
    @SerialName("turno_departamento_codigo") val turnoDepartamentoCodigo: String,
    @SerialName("turno_hospital_codigo") val turnoHospitalCodigo: String,
    @SerialName("paciente_numero_historia_clinica") val pacienteNumeroHistoriaClinica: String,
    @SerialName("paciente_unidad_codigo") val pacienteUnidadCodigo: String,
    @SerialName("paciente_departamento_codigo") val pacienteDepartamentoCodigo: String,
    @SerialName("paciente_hospital_codigo") val pacienteHospitalCodigo: String,
    @SerialName("fecha_hora") val fechaHora: String
)

