package auth

import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
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

            val session = session_actual()
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

    suspend fun logout() = withContext(Dispatchers.IO) { supabaseClient.auth.signOut() }

    suspend fun crear_usuario(username: String, passwordP: String): UserInfo? = withContext(Dispatchers.IO) {

        try {
            val userWithEmail = supabaseClient.auth.admin.createUserWithEmail {
                email = username
                password = passwordP
                autoConfirm = true
            }
            println("Usuario creado: $userWithEmail")
            userWithEmail
        } catch (e: Exception) {
            println("Error al crear el usuario: ${e.message}")
            null
        }

    }

    suspend fun cambiar_rol(uuid: String, rol: String): Boolean = withContext(Dispatchers.IO) {
//        "a2894038-e3a3-4f17-8938-2a456d0c2134"
//        "51ea96a0-6cee-4bca-ba4a-1702b214fac1"

        try {
            // Actualiza el rol del usuario
            val updatedUser = supabaseClient.auth.admin.updateUserById(uuid) {
                role = rol
            }
            println("Usuario actualizado: $updatedUser")
            true
        } catch (e: Exception) {
            println("Error al actualizar el usuario: ${e.message}")
            false
        }

    }

    suspend fun session_actual(): UserSession? = withContext(Dispatchers.IO) { supabaseClient.auth.currentSessionOrNull() }





}