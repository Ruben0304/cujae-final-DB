package dao

import auth.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import modelos.PatientRequest
import modelos.Turno
import modelos.UserConMetadata
import modelos.UserMetadata
import supabase.Supabase
import vistas.componentes.ToastManager
import vistas.componentes.ToastType

object AccountDAO {

    private suspend fun obtenerRolyHospital(rol: String, hospital: String? = null) = withContext(Dispatchers.IO) {

        val tabla = Supabase.coneccion.from("usuarios_metadata")

        try {
            if (hospital != null) {
                tabla.select {
                    filter {
                        eq("rol", rol)
                        eq("hospital_id", hospital)
                    }
                }.decodeList<UserMetadata>()
            } else {
                tabla.select {
                    filter {
                        eq("rol", rol)
                    }
                }.decodeList<UserMetadata>()
            }

        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }

    }

    suspend fun obtenerUsers(rol: String, hospital: String? = null) = withContext(Dispatchers.IO) {
        try {

            val rolSupabase = if (rol == "medico") "authenticated" else "service_role"
            // Obtenemos ambas listas
            val admins =
                Supabase.coneccion.auth.admin.retrieveUsers().filter { userInfo -> userInfo.role == rolSupabase }


            val userMetadataList = if (rol == "medico") obtenerRolyHospital(rol,hospital) ?: emptyList() else obtenerRolyHospital(rol) ?: emptyList()
            val hospitales = HospitalDAO.getHospitals()

            // Crear una lista vacía donde guardaremos los objetos combinados
            val listaCombinada = mutableListOf<UserConMetadata>()

            // Recorremos la lista de admins
            for (u in userMetadataList) {
                // Buscamos en la lista de UserMetadata por el id del admin
                val userMetadata = admins.find { it.id == u.user_id }

                // Si encontramos un UserMetadata con el mismo id, combinamos la información
                if (userMetadata != null) {
                    // Creamos un nuevo objeto AdminConMetadata con los datos combinados
                    val combinado = UserConMetadata(
                        id = u.user_id,
                        nombre = "${u.nombre} ${u.apellidos}",
                        hospital = hospitales.find { it.codigo == u.hospital_id }?.nombre,
                        email = userMetadata.email
                    )
                    // Agregamos el objeto combinado a la lista
                    listaCombinada.add(combinado)
                }
            }

            listaCombinada // Retornamos la lista combinada
        } catch (e: Exception) {
            println("Error: ${e.message}")
            emptyList() // En caso de error, devuelve una lista vacía
        }
    }

    suspend fun vincularMedicoCuenta(medico: String, usuario:String) = withContext(Dispatchers.IO) {
        try {
            Supabase.coneccion.from("usuario_medico").insert(mapOf("medico_id" to medico, "user_id" to usuario))
        }catch (e: Exception){
            println(e.message)
            null
        }

    }

}