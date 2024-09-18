package dao

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Consulta
import modelos.ConsultaRequest
import supabase.Consul
import supabase.Supabase
import java.time.LocalDate
import java.time.LocalDateTime

object ConsultaDAO {
    suspend fun getConsultasTurno(
        unidadCodigo: String,
        departamentoCodigo: String,
        hospital: String,
        turnoNumero: Int,
        medico: String
    ) = withContext(Dispatchers.IO) {

        try {
            val columns = Columns.raw(
                """
    consulta_id,
    fecha_hora,
    registro (
    registro_id,
    unidad_codigo,
    departamento_codigo,
    hospital_codigo,
      paciente_id,
      estado,
      paciente(
      ci,
      nombre,
      apellidos
      )
    )
""".trimIndent()
            )
            Supabase.coneccion.from("consulta")
                .select(columns = columns) {
                    filter {
                        and {
                            eq("turno_unidad_codigo", unidadCodigo)
                            eq("turno_departamento_codigo", departamentoCodigo)
                            eq("turno_hospital_codigo", hospital)
                            eq("turno_numero", turnoNumero)
                            eq("id_medico", medico)


                        }
                    }
                }.decodeList<Consulta>()

        } catch (e: Exception) {
            println(e.message)
            emptyList()
        }

    }

    suspend fun getConsultasMedico() = withContext(Dispatchers.IO) {

        try {
            val columns = Columns.raw(
                """
    consulta_id,
    fecha_hora,
    turno_numero,
    id_medico,
    registro (
    registro_id,
    unidad_codigo,
    departamento_codigo,
    hospital_codigo,
      paciente_id,
      estado,
      paciente(
      ci,
      nombre,
      apellidos
      )
    )
""".trimIndent()
            )
            Supabase.coneccion.from("consulta")
                .select(columns = columns)
                .decodeList<Consulta>()

        } catch (e: Exception) {
            println(e.message)
            emptyList()
        }

    }

    fun filtrarConsultasHoy(consultas: List<Consulta>):Int {
        val hoy = LocalDate.now()
        return consultas.filter { consulta ->
            val fechaConsulta = LocalDateTime.parse(consulta.fecha_hora).toLocalDate()
            fechaConsulta == hoy
        }.count()
    }

    fun obtenerProximaConsulta(consultas: List<Consulta>): Consulta? {
        val ahora = LocalDateTime.now()
        return consultas
            .filter { LocalDateTime.parse(it.fecha_hora).isAfter(ahora) }
            .minByOrNull { LocalDateTime.parse(it.fecha_hora) }
    }





    @Serializable
    data class CrearConsultaRequest(
        val p_turno_numero: Int,
        val p_turno_unidad_codigo: String,
        val p_turno_departamento_codigo: String,
        val p_turno_hospital_codigo: String,
        val p_paciente_numero_historia_clinica: String,
        val p_paciente_unidad_codigo: String,
        val p_paciente_departamento_codigo: String,
        val p_paciente_hospital_codigo: String,
        val p_fecha_hora: String // Asegúrate de que el formato de la fecha sea compatible
    )

    suspend fun crearConsulta(
        turnoNumero: Int,
        turnoUnidadCodigo: String,
        turnoDepartamentoCodigo: String,
        turnoHospitalCodigo: String,
        pacienteNumeroHistoriaClinica: String,
        pacienteUnidadCodigo: String,
        pacienteDepartamentoCodigo: String,
        pacienteHospitalCodigo: String,
        fechaHora: String
    ) = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "crear_consulta",
            CrearConsultaRequest(
                p_turno_numero = turnoNumero,
                p_turno_unidad_codigo = turnoUnidadCodigo,
                p_turno_departamento_codigo = turnoDepartamentoCodigo,
                p_turno_hospital_codigo = turnoHospitalCodigo,
                p_paciente_numero_historia_clinica = pacienteNumeroHistoriaClinica,
                p_paciente_unidad_codigo = pacienteUnidadCodigo,
                p_paciente_departamento_codigo = pacienteDepartamentoCodigo,
                p_paciente_hospital_codigo = pacienteHospitalCodigo,
                p_fecha_hora = fechaHora
            )
        )
    }

    @Serializable
    data class ConsultaRequestDelete(
        val p_codigo_unidad: String,
        val p_codigo_departamento: String,
        val p_codigo_hospital: String,
        val p_numero_consulta: Int,
        val p_numero_turno: Int,
        val p_codigo_medico: String
    )

    suspend fun eliminar(u: String, d: String, h: String, m: String, t: Int, c: Int) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "eliminar_consulta",
                ConsultaRequestDelete(u, d, h, c, t, m)
            )
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
        }

    }

    suspend fun crearConsultaConRegistro(consulta: ConsultaRequest) {
        try {
            println(consulta)
            Supabase.coneccion.postgrest.rpc("crear_consulta_con_registro", consulta)
            println(consulta)
        }catch (e: Exception){
            println(e.message)
        }

    }
}
