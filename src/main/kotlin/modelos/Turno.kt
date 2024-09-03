package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Turno(
    @SerialName("numero_turno")
    val numero: Int,
    @SerialName("pacientes_asignados")
    val totalPacientes: Int,
    @SerialName("pacientes_atendidos")
    val pacientesAtendidos: Int,
    @SerialName("medico_codigo")
    val doctor: String
) {
    fun esExitoso(): Boolean {
        return (pacientesAtendidos / totalPacientes.toFloat()) >= 0.8f
    }
}