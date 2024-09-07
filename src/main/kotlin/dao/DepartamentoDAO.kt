package dao

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Departamento
import supabase.Supabase


object DepartamentoDAO {

    @Serializable
    data class ODPRequest(val p_hospital_codigo: String)

    suspend fun obtenerDepartamentosPorHospital(p_hospital_codigo: String): List<Departamento> = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc("obtener_departamentos_por_hospital", ODPRequest(p_hospital_codigo)).decodeList<Departamento>()
    }
    @Serializable
    data class CrearDepartamentoRequest(
        val p_codigo: String,
        val p_nombre: String,
        val p_hospital_codigo: String
    )

    suspend fun crearDepartamento(
        codigo: String,
        nombre: String,
        hospitalCodigo: String
    ) = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "crear_departamento",
            CrearDepartamentoRequest(
                p_codigo = codigo,
                p_nombre = nombre,
                p_hospital_codigo = hospitalCodigo
            )
        )
    }
}