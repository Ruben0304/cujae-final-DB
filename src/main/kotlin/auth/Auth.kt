package auth

import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserSession
import repository.Supabase


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object Auth {
    private val supabaseClient = Supabase.coneccion

    suspend fun login(username: String, passwordP: String): UserSession? = withContext(Dispatchers.IO) {
        try {
            supabaseClient.auth.signInWith(Email) {
                email = username
                password = passwordP
            }

            // Verificar si se inició sesión correctamente
            val session = supabaseClient.auth.currentSessionOrNull()
            if (session != null) {
                println("Login successful")
                session
            } else {
                println("Autenticación fallida: Credenciales incorrectas")
                null
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }
}