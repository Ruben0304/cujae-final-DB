package dao

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.*
import supabase.Supabase
import vistas.componentes.ToastManager
import vistas.componentes.ToastType

object PatientDAO {

    @Serializable
    data class PURequest(
        val p_departamento_codigo: String,
        val p_hospital_codigo: String,
        val p_unidad_codigo: String
    )

    suspend fun listar_pacientes_por_unidad(
        hospitalId: String,
        departamentoId: String,
        unidadId: String
    ): List<Patient> {
        try {
            return Supabase.coneccion.postgrest.rpc(
                "obtener_pacientes_por_unidad_departamento_hospital",
                PURequest(unidadId, departamentoId, hospitalId)
            ).decodeList<Patient>()
        } catch (e: Exception) {
            ToastManager.showToast(e.message.toString(), ToastType.ERROR)
            return emptyList()
        }

    }


    @Serializable
    data class CrearPacienteRequest(
        val p_numero_historia_clinica: String,
        val p_nombre: String,
        val p_apellidos: String,
        val p_fecha_nacimiento: String, // Asegúrate de que el formato de la fecha sea compatible
        val p_direccion: String,
        val p_unidad_codigo: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String
    )

    suspend fun crearPaciente(
        numeroHistoriaClinica: String,
        nombre: String,
        apellidos: String,
        fechaNacimiento: String,
        direccion: String,
        unidadCodigo: String,
        departamentoCodigo: String,
        hospitalCodigo: String
    ) = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "crear_paciente",
            CrearPacienteRequest(
                p_numero_historia_clinica = numeroHistoriaClinica,
                p_nombre = nombre,
                p_apellidos = apellidos,
                p_fecha_nacimiento = fechaNacimiento,
                p_direccion = direccion,
                p_unidad_codigo = unidadCodigo,
                p_departamento_codigo = departamentoCodigo,
                p_hospital_codigo = hospitalCodigo
            )
        )
    }

    suspend fun getPacientes() =
        withContext(Dispatchers.IO) {
            try {
                Supabase.coneccion.from("paciente")
                    .select()
                    .decodeList<PatientTable>()
            } catch (e: Exception) {
                println(e.message)
                emptyList()
            }

        }

    suspend fun comprobarPaciente(id: String) =
        withContext(Dispatchers.IO) {

            try {
                val count: Int = Supabase.coneccion.from("paciente")
                    .select {
                        filter {
                            eq("ci", id)
                        }
                    }.decodeList<PacienteParaComprobar>().size
                count == 1

            } catch (e: Exception) {
                println(e.message)
                false
            }

        }


    @Serializable
    data class PNARequest(val p_hospital_codigo: String)

    @Serializable
    data class PacientesNoAtendidos(
        val hospital_nombre: String,
        val departamento_nombre: String,
        val unidad_nombre: String,
        val numero_turno: Int,
        val total_pacientes_no_atendidos: Int,
        val numero_historia_clinica: String,
        val paciente_nombre: String,
        val paciente_apellidos: String,
        val direccion: String,
        val causa: String
    )

    suspend fun obtenerPacientesNoAtendidosPorHospital(p_hospital_codigo: String): List<PacientesNoAtendidos> =
        withContext(Dispatchers.IO) {
            Supabase.coneccion.postgrest.rpc("pacientes_no_atendidos_por_hospital", PNARequest(p_hospital_codigo))
                .decodeList<PacientesNoAtendidos>()
        }


    @Serializable
    data class OPCSRequest(
        val p_unidad_codigo: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String
    )

    suspend fun obtenerPacientesConEstadoYCausa(
        unidadCodigo: String,
        departamentoCodigo: String,
        hospitalCodigo: String
    ): List<Registro> = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.postgrest.rpc(
                "obtener_pacientes_con_estado_y_causa",
                OPCSRequest(unidadCodigo, departamentoCodigo, hospitalCodigo)
            )
                .decodeList<Registro>()
        } catch (e: Exception) {
//            ToastManager.showToast(e.message.toString(), ToastType.ERROR)
            println("errorrr :" + e.message)
            emptyList()
        }

    }

    suspend fun insert(patient: PatientRequest) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.from("paciente").insert(patient) {
                select()
            }.decodeSingle<PatientRequest>()
        } catch (e: Exception) {
            println(e.message)
            null
        }

    }

}
