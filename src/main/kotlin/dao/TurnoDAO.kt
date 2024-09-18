package dao

import dao.SearchDAO.Consulta

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Turno
import supabase.Supabase

object TurnoDAO {


    suspend fun getTurnosMedico() =
        withContext(Dispatchers.IO) {
            try {
                Supabase.coneccion.postgrest.rpc("obtener_turnos_admin").decodeList<Turno>()
            } catch (e: Exception) {
                println(e.message)
                emptyList()
            }

        }

    suspend fun getTurnosUnidad(unidadCodigo: String, departamentoCodigo: String, hospital: String) =
        withContext(Dispatchers.IO) {
            try {
                Supabase.coneccion.postgrest.rpc("obtener_listado_turnos", mapOf("p_unidad_codigo" to unidadCodigo,"p_departamento_codigo" to departamentoCodigo, "p_hospital_codigo" to hospital)).decodeList<Turno>()
            } catch (e: Exception) {
                println(e.message)
                emptyList()
            }

        }



    suspend fun eliminar(numeroTurno: Int,unidadCodigo: String, departamentoCodigo: String, hospital: String,medicoCodigo: String) =
        withContext(Dispatchers.IO) {
            try {
                Supabase.coneccion.from("turno")
                    .delete { filter {
                        and {
                            eq("numero_turno", numeroTurno)
                            eq("hospital_codigo", hospital )
                            eq("departamento_codigo", departamentoCodigo )
                            eq("unidad_codigo", unidadCodigo )
                            eq("medico_codigo", medicoCodigo )

                        }
                    } }
            } catch (e: Exception) {
                println(e.message)
            }

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