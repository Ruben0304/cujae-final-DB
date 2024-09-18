package modelos

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter



@Serializable
data class PacienteC(
    val ci: String,
    val nombre: String,
    val apellidos: String
)

@Serializable
data class RegistroC(
    val paciente_id: String,
    val registro_id: Long,
    val unidad_codigo: String,
    val departamento_codigo: String,
    val hospital_codigo: String,
    val estado: String,
    val paciente: PacienteC
)

@Serializable
data class ConsultaRequest(
    @SerialName("p_medico_codigo")
    val codigo: String,
    @SerialName("p_fecha_hora")
    val timestamp: String,
    @SerialName("p_paciente_ci")
    val registro: String
) {
    // Método auxiliar para formatear la fecha actual
    companion object {
        fun crearConTimestampActual(codigo: String, registro: String): ConsultaRequest {
            val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val timestampActual = java.time.LocalDateTime.now().format(formato)
            return ConsultaRequest(codigo, timestampActual, registro)
        }
    }
}

@Serializable
data class Consulta(
    val consulta_id: Int,
    val fecha_hora: String,
    val turno_numero: Int,
    val id_medico: String,
    val registro: RegistroC
){
    fun getFormattedFechaHora(): String {
        val localDateTime = LocalDateTime.parse(fecha_hora)
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a")
        return localDateTime.toJavaLocalDateTime().format(formatter)
    }

}



//@Serializable
//data class Consulta(
//    @SerialName("consulta_id") val consultaId: Int,
//    @SerialName("turno_numero") val turnoNumero: Int,
//    @SerialName("turno_unidad_codigo") val turnoUnidadCodigo: String,
//    @SerialName("turno_departamento_codigo") val turnoDepartamentoCodigo: String,
//    @SerialName("turno_hospital_codigo") val turnoHospitalCodigo: String,
//    @SerialName("id_registro_paciente") val pacienteNumeroHistoriaClinica: Long,
//    @SerialName("unidad_reg_pac") val pacienteUnidadCodigo: String,
//    @SerialName("departamento_reg_pac") val pacienteDepartamentoCodigo: String,
//    @SerialName("hospital_reg_pac") val pacienteHospitalCodigo: String,
//    @SerialName("fecha_hora") val fechaHora: String,
//    @SerialName("id_medico") val idMedico: String
//
//
//) {}


