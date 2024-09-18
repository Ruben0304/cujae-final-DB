package modelos

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter

@Serializable
data class Informe(
    @SerialName("numero_informe")
    val numeroInforme: Long,

    @SerialName("unidad_codigo")
    val unidadCodigo: String,

    @SerialName("departamento_codigo")
    val departamentoCodigo: String,

    @SerialName("hospital_codigo")
    val hospitalCodigo: String,

    @SerialName("fecha_hora")
    val fechaHora: String,

    @SerialName("pacientes_atendidos")
    val pacientesAtendidos: Int,

    @SerialName("pacientes_alta")
    val pacientesAlta: Int,

    @SerialName("pacientes_admitidos")
    val pacientesAdmitidos: Int,

    @SerialName("total_registro")
    val totalRegistro: Int
){
    fun getFormattedFechaHora(): String {
        val localDateTime = LocalDateTime.parse(fechaHora)
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a")
        return localDateTime.toJavaLocalDateTime().format(formatter)
    }
}

