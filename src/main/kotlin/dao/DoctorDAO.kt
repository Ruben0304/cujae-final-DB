package dao

import dao.UnidadDAO.OUXHDRequest
import database.Database
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Doctor
import repository.Supabase


object DoctorDAO {

    @Serializable
    data class MHRequest(val p_hospital_codigo: String)

    @Serializable
    data class MURequest(val  p_unidad_codigo:String, val p_departamento_codigo:String , val p_hospital_codigo: String)

    @Serializable
    data class MDRequest(val  p_departamento_codigo:String , val p_hospital_codigo: String)

    suspend fun listar_medicos_por_hospital(hospitalId: String): List<Doctor> = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "listar_medicos_por_hospital",
            MHRequest(hospitalId)
        ).decodeList<Doctor>()
    }

    suspend fun listar_medicos_por_departamento(depaId: String, hospiId: String): List<Doctor> = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "listar_medicos_por_departamento",
            MDRequest(depaId,hospiId)
        ).decodeList<Doctor>()
    }

    suspend fun listar_medicos_por_unidad(unidadId: String,depaId: String, hospiId: String): List<Doctor> = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "listar_medicos_por_unidad",
            MURequest(unidadId,depaId,hospiId)
        ).decodeList<Doctor>()
    }

    @Serializable
    data class CrearMedicoRequest(
        val p_codigo: String,
        val p_nombre: String,
        val p_apellidos: String,
        val p_especialidad: String,
        val p_numero_licencia: String,
        val p_telefono: String,
        val p_anios_experiencia: Int,
        val p_datos_contacto: String,
        val p_unidad_codigo: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String
    )

    suspend fun crearMedico(
        codigo: String,
        nombre: String,
        apellidos: String,
        especialidad: String,
        numeroLicencia: String,
        telefono: String,
        aniosExperiencia: Int,
        datosContacto: String,
        unidadCodigo: String,
        departamentoCodigo: String,
        hospitalCodigo: String
    ) = withContext(Dispatchers.IO) {
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
    }


}
