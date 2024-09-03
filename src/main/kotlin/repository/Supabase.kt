package repository

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.graphql.GraphQL
import io.github.jan.supabase.graphql.graphql
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.ApolloClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.put
import modelos.Departamento
import modelos.Hospital


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

//    supabaseClient.from("departamento").insert(generarDepartamentosFicticios())
//
//        for (p in supabaseClient.from("departamento").select().decodeList<Departamento>())
//        println(p.nombre)

//    for (p in supabaseClient.from("hospital").select().decodeList<repository.Hospital>())
//        println(p.codigo)
//
//
//
    for (p in supabaseClient.auth.admin.retrieveUsers())
        println(p.email)

}

