package dao

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Turno
import modelos.Unidad
import supabase.Supabase
import vistas.componentes.ToastManager
import vistas.componentes.ToastType


object UnidadDAO {

    @Serializable
    data class OUXHDRequest(val p_hospital_codigo: String, val p_departamento_codigo: String)

    suspend fun obtenerUnidadesPorHospitalYDepartamento(
        hospitalCodigo: String,
        departamentoCodigo: String
    ) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.from("unidad")
                .select { filter {
                    and {
                        eq("hospital_codigo", hospitalCodigo )
                        eq("departamento_codigo", departamentoCodigo )
                        eq("activo", true )

                    }
                } }
                .decodeList<Unidad>()
        } catch (e: Exception) {
            ToastManager.showToast(e.message.toString(), ToastType.ERROR)
            println(e.message)
            emptyList()
        }
//        try {
//            Supabase.coneccion.postgrest.rpc(
//                "obtener_unidades_por_hospital_y_departamento",
//                OUXHDRequest(hospitalCodigo, departamentoCodigo)
//            ).decodeList<Unidad>()
//        } catch (e: Exception) {
//            ToastManager.showToast(e.message.toString(), ToastType.ERROR)
//            emptyList()
//        }


    }

    suspend fun obtenerUnidadesHospital(hospitalCodigo: String) = withContext(Dispatchers.IO) {
            try {
                Supabase.coneccion.from("unidad")
                    .select { filter {
                        and {
                            eq("hospital_codigo", hospitalCodigo )
                            eq("activo", true )
                        }
                    } }
                    .decodeList<Unidad>()
            } catch (e: Exception) {
                ToastManager.showToast(e.message.toString(), ToastType.ERROR)
                println(e.message)
                emptyList()
            }

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



    suspend fun eliminar(u:String ,d: String, h: String)= withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("eliminar_unidad", mapOf("p_codigo" to u,"p_departamento_codigo" to d, "p_hospital_codigo" to h))
        }catch (e: Exception){
            println(e.message)
        }

    }
}