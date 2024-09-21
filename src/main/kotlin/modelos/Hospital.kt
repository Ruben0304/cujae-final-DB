package modelos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


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
data class HResumenProcesoResult(
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


//@Serializable
//data class HResumenProcesoHospitalResult(
//    val hospital: String,
//
//    val numero_turno: Long,
//    //@Serializable(with = FechaHoraSerializer::class) // Usar el custom serializer
////    val hora_informe: String,
//    val pacientes_inicio: Long,
//    val pacientes_atendidos: Long,
//    val pacientes_total: Long,
//    val porcentaje_atendidos: Double,
//    val pacientes_no_atendidos: Long,
//    val pacientes_alta: Long,
//    val no_atendidos_extranjero: Long,
//    val no_atendidos_fuera_provincia: Long,
//    val no_atendidos_hospitalizados: Long,
//    val no_atendidos_otras_causas: Long,
//    val no_atendidos_causa_desconocida: Long
//)
//
//object FechaHoraSerializer : KSerializer<String> {
//    private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a")
//
//    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FormattedDateTime", PrimitiveKind.STRING)
//
//    override fun serialize(encoder: Encoder, value: String) {
//        // Al serializar, no necesitas modificar el valor.
//        encoder.encodeString(value)
//    }
//
//    override fun deserialize(decoder: Decoder): String {
//        // Al deserializar, formatea la fecha automáticamente.
//        val rawDate = decoder.decodeString()
//        val localDateTime = java.time.LocalDateTime.parse(rawDate)
//        return localDateTime.format(formatter)
//    }
//
//}


@Serializable
data class HResumenConsultasExitosasParam(
    @SerialName("p_hospital_codigo") val hospital: String
)


@Serializable
data class HResumenConsultasExitosasResult(
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
data class HPacientesNoAtendParam(
    @SerialName("p_hospital_codigo") val hospital: String

)


@Serializable
data class HPacientesNoAtendResult(
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






