package dao

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.*
import supabase.Supabase
import vistas.componentes.ToastManager
import vistas.componentes.ToastType

object HospitalDAO {

    suspend fun getAllHospitals() = withContext(Dispatchers.IO) {
        try {
             Supabase.coneccion.postgrest.rpc("resumen_por_hospitales"){
            }.decodeList<Hospital>()
        } catch (e: Exception) {
           println(e.message)
            emptyList()
        }
    }

    suspend fun getHospitals() = withContext(Dispatchers.IO) {
        try {
             Supabase.coneccion.from("hospital").select()
                .decodeList<HospitalNombres>()
        } catch (e: Exception) {
             emptyList()
        }

    }


    @Serializable
    data class CrearHospitalRequest(
        val p_codigo: String,
        val p_nombre: String
    )

    suspend fun crearHospital(
        codigo: String,
        nombre: String
    ) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "crear_hospital",
                CrearHospitalRequest(
                    p_codigo = codigo,
                    p_nombre = nombre
                )
            )
        } catch (e: Exception) {
            ToastManager.showToast(e.message.toString(), ToastType.ERROR)
        }

    }


    suspend fun eliminar(codigo: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.from("hospital").update({
                set("activo", false)
            }) {
                filter {
                    eq("codigo", codigo)
                }
            }
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
        }


    }

    suspend fun resumenProceso(h: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("resumen_proceso_hospital", HResumenProcesoParam(h)).decodeList<HResumenProcesoResult>()
        } catch (e: Exception){
            println(e.message)
            null
        }
    }

    suspend fun resumenConsultasExitosas(h: String)= withContext(Dispatchers.IO){
        try {
      Supabase.coneccion.postgrest.rpc("listado_unidades_consultas_exitosas_por_hospital", HResumenConsultasExitosasParam(h)).decodeList<HResumenConsultasExitosasResult>()
        }catch (e: Exception){
            println(e.message)
            null
        }
    }

    suspend fun pacientesNoAtendidos(h: String)= withContext(Dispatchers.IO){
        try {
            Supabase.coneccion.postgrest.rpc("pacientes_no_atendidos_por_hospital",
                HPacientesNoAtendParam(h)
            ).decodeList<HPacientesNoAtendResult>()
        }catch (e: Exception){
            println(e.message)
            null
        }
    }

}
