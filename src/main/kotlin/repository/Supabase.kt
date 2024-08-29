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
import kotlinx.serialization.json.put


object Supabase {

    const val supabaseUrl = "https://jhxtxukwfkcuzeqxgrtf.supabase.co"
    const val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpoeHR4dWt3ZmtjdXplcXhncnRmIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcyNDg4MDk0OCwiZXhwIjoyMDQwNDU2OTQ4fQ.Ug9OAR-q7y-nGb6TdZ-oI9dFbS2NXZ7OHlMqik_lQFg"

    // Inicialización del cliente Supabase como un lazy property
    val coneccion by lazy {
        createSupabaseClient(supabaseUrl, supabaseKey) {
            defaultSerializer = KotlinXSerializer()
            install(Postgrest) {
                defaultSchema = "public" // default: "public"
                propertyConversionMethod = PropertyConversionMethod.SERIAL_NAME
            }
            install(Auth)
        }
    }
}


@Serializable
data class Loco(val id: Int, val nombre: String)


fun main() = runBlocking {

    val supabaseClient = Supabase.coneccion

    try {
         supabaseClient.auth.signInWith(Email) {
            email = "example@email3.com"
            password = "secretpassword3"
        }

        // Verificar si se inició sesión correctamente
        val session = supabaseClient.auth.currentSessionOrNull()
        if (session != null) {
           println(session)
        } else {
            println("Autenticación fallida: Credenciales incorrectas")
        }
    } catch (e: Exception) {
        println("Error: ${e.message}$")

    }

//    val userWithEmail = supabase.auth.admin.createUserWithEmail {
//        email = "example@email3.com"
//        password = "secretpassword3"
//        autoConfirm = true
//    }

//    try {
//        // Actualiza el rol del usuario
//        val updatedUser = supabase.auth.admin.updateUserById("534b968c-1bfd-4c5d-b6f4-01b8d26ede89") {
//            role = "authenticated"
//        }
//        println("Usuario actualizado: $updatedUser")
//    } catch (e: Exception) {
//        println("Error al actualizar el usuario: ${e.message}")
//    }

////
//    val session = supabase.auth.currentSessionOrNull()
//    println(session)
//        for (p in supabase.from("Loco").select().decodeList<Loco>())
//        println(p.nombre)





}
