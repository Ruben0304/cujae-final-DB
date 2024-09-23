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
            Supabase.coneccion.postgrest.rpc("resumen_por_hospitales") {
            }.decodeList<Hospital>()
        } catch (e: Exception) {
            println(e.message)
            emptyList()
        }
    }

    suspend fun getHospitals() = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.from("hospital").select{
                filter {
                    eq("activo", true)
                }
            }
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
            Supabase.coneccion.postgrest.rpc("resumen_proceso_hospital", HResumenProcesoParam(h))
                .decodeList<ResumenProcesoResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun resumenConsultasExitosas(h: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "listado_unidades_consultas_exitosas_por_hospital",
                HResumenConsultasExitosasParam(h)
            ).decodeList<HResumenConsultasExitosasResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }


    suspend fun hospConMasPacient() = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("top_5_hospitales_con_mas_de_100_pacientes")
                .decodeList<HMasPacientesResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun resumen() = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("resumen_por_hospitales").decodeList<ResumenResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun listadoMedicos(h: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("listado_de_medicos_por_hospital", ListadoMedParam(h))
                .decodeList<ListadoMedResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun listadoMedicos(d: String, h: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("listado_de_medicos_por_departamento_hospital", ListadoMedParamD(d, h))
                .decodeList<ListadoMedResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun listadoMedicos(u: String, d: String, h: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "listado_de_medicos_por_unidad_departamento_hospital",
                ListadoMedParamU(u, d, h)
            ).decodeList<ListadoMedResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun listadoPacientes(h: String, d: String, u: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("listado_de_pacientes", ListadoPacParamU(h, d, u))
                .decodeList<ListadoPacientesResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun listadoPacientes(h: String, d: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("listado_de_pacientes", ListadoPacParamD(h, d))
                .decodeList<ListadoPacientesResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun listadoPacientes(h: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("listado_de_pacientes", ListadoPacParamH(h))
                .decodeList<ListadoPacientesResult>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }
}
