package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Turno(
    @SerialName("numero_turno") val numeroTurno: Int,
    @SerialName("fecha") val fecha: String,  // Puede ser `String` o `LocalDate`
    @SerialName("medico_codigo") val medicoCodigo: String,
    @SerialName("medico_nombre") val medicoNombre: String?,
    @SerialName("medico_apellidos") val medicoApellidos: String?,
    @SerialName("cantidad_consultas") val totalPacientes: Long,
    @SerialName("pacientes_atendidos") val pacientesAtendidos: Long,
    @SerialName("pacientes_no_atendidos") val pacientesNoAtendidos: Long,
    @SerialName("unidad_codigo") val unidad: String? = null,
    @SerialName("departamento_codigo") val departamento: String? = null,
) {
    fun esExitoso(): Boolean {
        return (pacientesAtendidos / totalPacientes.toFloat()) >= 0.8f
    }
}