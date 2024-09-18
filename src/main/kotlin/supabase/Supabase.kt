package supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

import dao.HospitalDAO

import io.github.jan.supabase.postgrest.*
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.Json
import vistas.componentes.ToastManager
import vistas.componentes.ToastType


object Supabase {

    const val supabaseUrl = "https://wgfdmgsbsqflcgtecglw.supabase.co"
    const val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndnZmRtZ3Nic3FmbGNndGVjZ2x3Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcyNDk2NjU3NSwiZXhwIjoyMDQwNTQyNTc1fQ.44s9ndcvYmbaFE1iJWwvrFjdcK8Z0PjEihGVS-WP6Nk"

    // Inicialización del cliente Supabase como un lazy property
    val coneccion by lazy {
        createSupabaseClient(supabaseUrl, supabaseKey) {
            defaultSerializer = KotlinXSerializer(
                json = Json { ignoreUnknownKeys = true }
            )
            install(Postgrest) {
                defaultSchema = "public" // default: "public"
                propertyConversionMethod = PropertyConversionMethod.SERIAL_NAME
            }
            install(Auth)
        }

    }

}


@Serializable
data class Hospital(val codigo: String, val nombre: String)

@Serializable
data class Regis(val paciente_id: String, val estado: String)

@Serializable
data class Consul(val consulta_id: Int, val registro: Regis)

@Serializable
data class Reg(val ci: String)

fun main() = runBlocking {

    val supabaseClient = Supabase.coneccion
//    auth.Auth.login("m@m.com", "m")
//    for (r in supabaseClient.from("paciente").select().decodeList<Reg>()){
//        println(r.ci)
//    }
//    auth.Auth.cambiar_rol("d1bcbe10-04bb-4386-a6b3-5d8b5b440f6f","medico")
//    println(auth.Auth.session_actual())
//    for (p in HospitalDAO.getAllHospitals())
//        println(p.nombre)


//    try {
//        val columns = Columns.raw(
//            """
//    consulta_id,
//    fecha_hora,
//    registro (
//      paciente_id,
//      estado,
//      paciente(
//      ci,
//      nombre,
//      apellidos
//      )
//    )
//""".trimIndent()
//        )
//        val consultas = supabaseClient.from("consulta")
//            .select(columns = columns)
//            .decodeList<Consul>()
//
//        for (c in consultas)
//            println("${c.consulta_id} ${c.registro.paciente_id}")
////        println( auth.Auth.cambiar_rol("fc568479-5527-44e0-89ce-06e162575b68","admin_general"))
//    } catch (e: Exception) {
//        println(e.message)
//    }

}

