package dao

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.CrearMedicoRequest
import modelos.Doctor
import modelos.DoctorId
import modelos.UniqueCodeMedico
import supabase.Supabase


object DoctorDAO {


    @Serializable
    data class MHRequest(val p_hospital_codigo: String)

    @Serializable
    data class MURequest(val p_unidad_codigo: String, val p_departamento_codigo: String, val p_hospital_codigo: String)

    @Serializable
    data class MDRequest(val p_departamento_codigo: String, val p_hospital_codigo: String)

    suspend fun listar_medicos_por_hospital(hospitalId: String): List<Doctor> = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "listar_medicos_por_hospital",
            MHRequest(hospitalId)
        ).decodeList<Doctor>()
    }

    suspend fun listar_medicos_por_departamento(depaId: String, hospiId: String) = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "listar_medicos_por_departamento",
            MDRequest(depaId, hospiId)
        ).decodeList<Doctor>()
    }

    suspend fun listar_medicos_por_unidad(unidadId: String, depaId: String, hospiId: String) =
        withContext(Dispatchers.IO) {
            Supabase.coneccion.postgrest.rpc(
                "listar_medicos_por_unidad",
                MURequest(unidadId, depaId, hospiId)
            ).decodeList<Doctor>()
        }

    suspend fun eliminarMedico(codigo: String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "eliminar_medico", mapOf("p_codigo" to codigo)
            )
        } catch (e: Exception) {
            println(e.message)
        }

    }


    suspend fun crearMedico(
        nombre: String,
        apellidos: String,
        especialidad: String,
        numeroLicencia: String,
        telefono: String,
        aniosExperiencia: Int,
        datosContacto: String,
        unidadCodigo: String? = null,
        departamentoCodigo: String? = null,
        hospitalCodigo: String
    ) = withContext(Dispatchers.IO) {
        try {
            val codigo = generarCodigoUnico().orEmpty()
            Supabase.coneccion.postgrest.rpc(
                "crear_medico",
                CrearMedicoRequest(
                    p_codigo = codigo,
                    p_nombre = nombre,
                    p_apellidos = apellidos,
                    p_especialidad = especialidad,
                    p_numero_licencia = numeroLicencia,
                    p_telefono = telefono,
                    p_anios_experiencia = aniosExperiencia,
                    p_datos_contacto = datosContacto,
                    p_unidad_codigo = unidadCodigo,
                    p_departamento_codigo = departamentoCodigo,
                    p_hospital_codigo = hospitalCodigo
                )
            )
            codigo
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }



    suspend fun generarCodigoUnico() = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc("generate_unique_medico_code").decodeAs<String>()
        } catch (e: Exception) {
            println(e.message)
            null
        }

    }


}
