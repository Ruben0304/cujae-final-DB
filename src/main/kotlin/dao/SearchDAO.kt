package dao
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import repository.Supabase

object SearchDAO {


    private val supabaseClient = Supabase.coneccion.postgrest

    @Serializable
    data class Causa(
        val causa_id: Int,
        val descripcion: String
    )

    @Serializable
    data class Consulta(
        val consulta_id: Int,
        val fecha_hora: String,  // Cambia a `LocalDateTime` si prefieres trabajar con tipos de fecha/hora de Kotlin.
        val turno_numero: Int,
        val paciente_numero_historia_clinica: String
    )

    @Serializable
    data class Hospital(
        val hospital_codigo: String,
        val hospital_nombre: String
    )

    @Serializable
    data class Medico(
        val medico_codigo: String,
        val medico_nombre: String,
        val medico_apellidos: String,
        val especialidad: String
    )

    @Serializable
    data class Paciente(
        val paciente_numero_historia_clinica: String,
        val paciente_nombre: String,
        val paciente_apellidos: String,
        val direccion: String
    )


    // Buscar Causas de No Atención
        suspend fun buscarCausasNoAtencion(criterio: String): List<Causa> = withContext(Dispatchers.IO) {
            supabaseClient.rpc("buscar_causas_no_atencion", mapOf("p_criterio" to criterio))
                .decodeList<Causa>()
        }

        // Buscar Consultas
        suspend fun buscarConsultas(criterio: String): List<Consulta> = withContext(Dispatchers.IO) {
            supabaseClient.rpc("buscar_consultas", mapOf("p_criterio" to criterio))
                .decodeList<Consulta>()
        }

        // Buscar Hospitales
        suspend fun buscarHospitales(criterio: String): List<Hospital> = withContext(Dispatchers.IO) {
            supabaseClient.rpc("buscar_hospitales", mapOf("p_criterio" to criterio))
                .decodeList<Hospital>()
        }

        // Buscar Médicos
        suspend fun buscarMedicos(criterio: String): List<Medico> = withContext(Dispatchers.IO) {
            supabaseClient.rpc("buscar_medicos", mapOf("p_criterio" to criterio))
                .decodeList<Medico>()
        }

        // Buscar Pacientes
        suspend fun buscarPacientes(criterio: String): List<Paciente> = withContext(Dispatchers.IO) {
            supabaseClient.rpc("buscar_pacientes", mapOf("p_criterio" to criterio))
                .decodeList<Paciente>()
        }


}