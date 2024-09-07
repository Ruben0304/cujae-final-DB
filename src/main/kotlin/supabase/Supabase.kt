package supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

import dao.HospitalDAO
import dao.cambiarEstadoEnRegistro
import dao.marcarNoAtendido
import io.github.jan.supabase.postgrest.*
import kotlinx.serialization.json.Json


object Supabase {

    const val supabaseUrl = "https://wgfdmgsbsqflcgtecglw.supabase.co"
    const val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndnZmRtZ3Nic3FmbGNndGVjZ2x3Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcyNDk2NjU3NSwiZXhwIjoyMDQwNTQyNTc1fQ.44s9ndcvYmbaFE1iJWwvrFjdcK8Z0PjEihGVS-WP6Nk"

    // Inicialización del cliente Supabase como un lazy property
    val coneccion by lazy {
        createSupabaseClient(supabaseUrl, supabaseKey) {
            defaultSerializer = KotlinXSerializer(
                json = Json { ignoreUnknownKeys = true  }
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


fun main() = runBlocking {

    val supabaseClient = Supabase.coneccion


   auth.Auth.login("b@b.com","b")
//    auth.Auth.cambiar_rol("d1bcbe10-04bb-4386-a6b3-5d8b5b440f6f","medico")
//    println(auth.Auth.session_actual())
//    for (p in HospitalDAO.getAllHospitals())
//        println(p.nombre)
    try{
        cambiarEstadoEnRegistro(54,"U002","D002","H001","alta")
        println("ok")
         }
    catch(e: Exception){
        println(e.message)
    }

}

