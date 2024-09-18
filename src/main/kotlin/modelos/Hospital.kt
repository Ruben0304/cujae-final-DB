package modelos

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import java.time.format.DateTimeFormatter

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*


@Serializable
data class Hospital(
    @SerialName("hospital_codigo") val codigo: String,
    @SerialName("hospital_nombre") val nombre: String,
    @SerialName("cantidad_departamentos") val cantidadDepartamentos: Int,
    @SerialName("cantidad_unidades") val cantidadUnidades: Int,
    @SerialName("cantidad_medicos") val cantidadMedicos: Int,
    @SerialName("cantidad_pacientes") val cantidadPacientes: Int
)

@Serializable
data class HospitalNombres(
     val codigo: String,
     val nombre: String,
)

@Serializable
data class HResumenProcesoParam(
    val p_hospital_codigo : String,
)

@Serializable
data class HResumenProcesoHospitalResult(
    val hospital: String,
    val numero_turno: Long,
    @Serializable(with = FechaHoraSerializer::class) // Usar el custom serializer
    val hora_informe: String,
    val pacientes_inicio: Long,
    val pacientes_atendidos: Long,
    val pacientes_total: Long,
    val porcentaje_atendidos: Double,
    val pacientes_no_atendidos: Long,
    val pacientes_alta: Long,
    val no_atendidos_extranjero: Long,
    val no_atendidos_fuera_provincia: Long,
    val no_atendidos_hospitalizados: Long,
    val no_atendidos_otras_causas: Long,
    val no_atendidos_causa_desconocida: Long
)

object FechaHoraSerializer : KSerializer<String> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a")

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FormattedDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        // Al serializar, no necesitas modificar el valor.
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        // Al deserializar, formatea la fecha automáticamente.
        val rawDate = decoder.decodeString()
        val localDateTime = java.time.LocalDateTime.parse(rawDate)
        return localDateTime.format(formatter)
    }
}



