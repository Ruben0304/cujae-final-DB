package dao

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Turno
import supabase.Supabase

object TurnoDAO {
    suspend fun getTurnosUnidad(unidadCodigo: String, departamentoCodigo: String, hospital: String): List<Turno> =
        withContext(Dispatchers.IO) {
            Supabase.coneccion.from("turno")
                .select { filter {
                    and {
                        eq("unidad_codigo", unidadCodigo)
                        eq("departamento_codigo", departamentoCodigo)
                        eq("hospital_codigo", hospital)
                    }

                } }
                .decodeList<Turno>()
        }

    @Serializable
    data class CrearTurnoRequest(
        val p_numero_turno: Int,
        val p_unidad_codigo: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String,
        val p_medico_codigo: String,
        val p_fecha: String, // Asegúrate de que el formato de la fecha sea compatible
        val p_pacientes_atendidos: Int,
        val p_pacientes_asignados: Int
    )

    suspend fun crearTurno(
        numeroTurno: Int,
        unidadCodigo: String,
        departamentoCodigo: String,
        hospitalCodigo: String,
        medicoCodigo: String,
        fecha: String,
        pacientesAtendidos: Int,
        pacientesAsignados: Int
    ) = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "crear_turno",
            CrearTurnoRequest(
                p_numero_turno = numeroTurno,
                p_unidad_codigo = unidadCodigo,
                p_departamento_codigo = departamentoCodigo,
                p_hospital_codigo = hospitalCodigo,
                p_medico_codigo = medicoCodigo,
                p_fecha = fecha,
                p_pacientes_atendidos = pacientesAtendidos,
                p_pacientes_asignados = pacientesAsignados
            )
        )
    }
}