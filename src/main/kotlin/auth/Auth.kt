package auth

import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserSession
import io.github.jan.supabase.postgrest.from
import supabase.Supabase


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.put
import modelos.UserMetadata
import supabase.Hospital


object Auth {
    private val supabaseClient = Supabase.coneccion
    private var sessionActive = false
    var rol: String = ""
    var hospital: String = ""
    var nombre: String? = null
    var apellidos: String? = null
    var idMedico: String? = null

    fun isSessionActive(): Boolean = sessionActive

    suspend fun verificarSesionAlIniciar() {
        sessionActive = session_actual() != null
    }

    suspend fun login(username: String, passwordP: String): UserSession? = withContext(Dispatchers.IO) {
        try {
            supabaseClient.auth.signInWith(Email) {
                email = username
                password = passwordP
            }

            val session = session_actual()
            if (session != null) {
                println("Login successful")
                val userData = session.user?.let { obtenerRolyHospital(it.id) }
                if (userData != null) {
                    rol = userData.rol
                    hospital = userData.hospital_id.toString()
                    nombre = userData.nombre
                    apellidos = userData.apellidos
                }
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


    suspend fun crear_usuario(
        username: String,
        passwordP: String,
        nombre: String,
        apellido: String,
        rol: String,
        hospital: String?
    ) =
        withContext(Dispatchers.IO) {

            try {
                val userWithEmail = supabaseClient.auth.admin.createUserWithEmail {
                    email = username
                    password = passwordP
                    autoConfirm = true
                }
                crearMetadataUsuario(
                    UserMetadata(
                        user_id = userWithEmail.id,
                        nombre = nombre,
                        apellidos = apellido,
                        rol = rol,
                        hospital_id = hospital
                    )
                )
                when(rol){
                    "admin_general","admin_hospital" -> {
                        cambiar_rol(userWithEmail.id,"service_role")
                    }
                }
                userWithEmail
            } catch (e: Exception) {
                println("Error al crear el usuario: ${e.message}")
            }

        }

    suspend fun cambiar_rol(uuid: String, rol: String): Boolean = withContext(Dispatchers.IO) {

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

    private suspend fun obtenerRolyHospital(uid: String) = withContext(Dispatchers.IO) {

        try {
            Supabase.coneccion.from("usuarios_metadata")
                .select {
                    filter {
                        eq("user_id", uid)
                    }
                }
                .decodeSingle<UserMetadata>()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }

    }


    private suspend fun crearMetadataUsuario(metadata: UserMetadata) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.from("usuarios_metadata").insert(metadata)
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }

    }


    suspend fun session_actual(): UserSession? = withContext(Dispatchers.IO) { supabaseClient.auth.currentSessionOrNull() }

    suspend fun logout() = withContext(Dispatchers.IO) { supabaseClient.auth.signOut() }

    suspend fun deleteUser(id:String) = withContext(Dispatchers.IO) {

            supabaseClient.auth.admin.deleteUser(id)

    }
}