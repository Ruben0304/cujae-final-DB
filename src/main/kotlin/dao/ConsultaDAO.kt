package dao

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Consulta
import modelos.Turno
import repository.Supabase

object ConsultaDAO {
    suspend fun getConsultasTurno(unidadCodigo: String, departamentoCodigo: String, hospital: String, turnoNumero:Int): List<Consulta> =
        withContext(Dispatchers.IO) {
            Supabase.coneccion.from("consulta")
                .select { filter {
                    and {
                        eq("turno_unidad_codigo", unidadCodigo)
                        eq("turno_departamento_codigo", departamentoCodigo)
                        eq("turno_hospital_codigo", hospital)
                        eq("turno_numero", turnoNumero)

                    }

                } }
                .decodeList<Consulta>()
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
}