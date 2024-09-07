package dao

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Unidad
import supabase.Supabase


object UnidadDAO {

    @Serializable
    data class OUXHDRequest(val p_hospital_codigo: String, val p_departamento_codigo: String)

    suspend fun obtenerUnidadesPorHospitalYDepartamento(
        hospitalCodigo: String,
        departamentoCodigo: String
    ): List<Unidad> = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "obtener_unidades_por_hospital_y_departamento",
            OUXHDRequest(hospitalCodigo, departamentoCodigo)
        ).decodeList<Unidad>()
    }
    @Serializable
    data class CrearUnidadRequest(
        val p_codigo: String,
        val p_nombre: String,
        val p_ubicacion: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String
    )

    suspend fun crearUnidad(
        codigo: String,
        nombre: String,
        ubicacion: String,
        departamentoCodigo: String,
        hospitalCodigo: String
    ) = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "crear_unidad",
            CrearUnidadRequest(
                p_codigo = codigo,
                p_nombre = nombre,
                p_ubicacion = ubicacion,
                p_departamento_codigo = departamentoCodigo,
                p_hospital_codigo = hospitalCodigo
            )
        )
    }
}