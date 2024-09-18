package dao

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import supabase.Supabase

object DAOs {
    @Serializable
    data class ActualizarConsultaRequest(
        val p_consulta_id: Int,
        val p_turno_numero: Int,
        val p_turno_unidad_codigo: String,
        val p_turno_departamento_codigo: String,
        val p_turno_hospital_codigo: String,
        val p_fecha_hora: String
    )

    @Serializable
    data class ActualizarDepartamentoRequest(
        val p_codigo: String,
        val p_hospital_codigo: String,
        val p_nombre: String
    )

    @Serializable
    data class ActualizarHospitalRequest(
        val p_codigo: String,
        val p_nombre: String
    )

    @Serializable
    data class ActualizarMedicoRequest(
        val p_codigo: String,
        val p_telefono: String,
        val p_anios_experiencia: Int,
        val p_datos_contacto: String,
        val p_unidad_codigo: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String
    )

    @Serializable
    data class ActualizarPacienteRequest(
        val p_registro_id: Int,
        val p_unidad_codigo: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String,
        val p_nueva_direccion: String
    )

    @Serializable
    data class ActualizarUnidadRequest(
        val p_codigo: String,
        val p_nombre: String,
        val p_ubicacion: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String
    )

    suspend fun actualizarConsulta(
        consultaId: Int,
        turnoNumero: Int,
        turnoUnidadCodigo: String,
        turnoDepartamentoCodigo: String,
        turnoHospitalCodigo: String,
        fechaHora: String
    ) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "actualizar_consulta",
                ActualizarConsultaRequest(
                    p_consulta_id = consultaId,
                    p_turno_numero = turnoNumero,
                    p_turno_unidad_codigo = turnoUnidadCodigo,
                    p_turno_departamento_codigo = turnoDepartamentoCodigo,
                    p_turno_hospital_codigo = turnoHospitalCodigo,
                    p_fecha_hora = fechaHora
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun actualizarDepartamento(
        codigo: String,
        hospitalCodigo: String,
        nombre: String
    ) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "actualizar_departamento",
                ActualizarDepartamentoRequest(
                    p_codigo = codigo,
                    p_hospital_codigo = hospitalCodigo,
                    p_nombre = nombre
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun actualizarHospital(
        codigo: String,
        nombre: String
    ) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "actualizar_hospital",
                ActualizarHospitalRequest(
                    p_codigo = codigo,
                    p_nombre = nombre
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun actualizarMedico(
        codigo: String,
        telefono: String,
        aniosExperiencia: Int,
        datosContacto: String,
        unidadCodigo: String,
        departamentoCodigo: String,
        hospitalCodigo: String
    ) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "actualizar_medico",
                ActualizarMedicoRequest(
                    p_codigo = codigo,
                    p_telefono = telefono,
                    p_anios_experiencia = aniosExperiencia,
                    p_datos_contacto = datosContacto,
                    p_unidad_codigo = unidadCodigo,
                    p_departamento_codigo = departamentoCodigo,
                    p_hospital_codigo = hospitalCodigo
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun actualizarPaciente(
        registroId: Int,
        unidadCodigo: String,
        departamentoCodigo: String,
        hospitalCodigo: String,
        nuevaDireccion: String
    ) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "actualizar_paciente",
                ActualizarPacienteRequest(
                    p_registro_id = registroId,
                    p_unidad_codigo = unidadCodigo,
                    p_departamento_codigo = departamentoCodigo,
                    p_hospital_codigo = hospitalCodigo,
                    p_nueva_direccion = nuevaDireccion
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun actualizarUnidad(
        codigo: String,
        nombre: String,
        ubicacion: String,
        departamentoCodigo: String,
        hospitalCodigo: String
    ) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "actualizar_unidad",
                ActualizarUnidadRequest(
                    p_codigo = codigo,
                    p_nombre = nombre,
                    p_ubicacion = ubicacion,
                    p_departamento_codigo = departamentoCodigo,
                    p_hospital_codigo = hospitalCodigo
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}