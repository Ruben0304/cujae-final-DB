package dao

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import supabase.Supabase

object CantidadesDAO {
    @Serializable
    data class ConteoPacientesEstado(
        val no_atendidos: Int,
        val atendidos: Int,
        val fallecidos: Int,
        val pendientes: Int,
        val dados_de_alta: Int,
        val cantidad_departamentos: Int,
        val cantidad_unidades: Int
    )

    @Serializable
    data class CPEHRequest(val p_hospital_codigo: String)

    suspend fun obtenerConteoPacientesPorEstado(hospitalCodigo: String): ConteoPacientesEstado = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc("conteo_pacientes_por_estado_hospital", CPEHRequest(hospitalCodigo))
            .decodeSingle<ConteoPacientesEstado>()
    }

}
