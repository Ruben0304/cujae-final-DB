package dao

import dao.DoctorDAO.MURequest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Doctor
import modelos.Hospital
import modelos.HospitalNombres
import supabase.Supabase

object HospitalDAO {

    suspend fun getAllHospitals(): List<Hospital> {
        return Supabase.coneccion.postgrest.rpc("resumen_por_hospitales")
            .decodeList<Hospital>()
    }

    suspend fun getHospitals(): List<HospitalNombres> {
        return Supabase.coneccion.from("hospital").select()
            .decodeList<HospitalNombres>()
    }


    @Serializable
    data class CrearHospitalRequest(
        val p_codigo: String,
        val p_nombre: String
    )

    suspend fun crearHospital(
        codigo: String,
        nombre: String
    ) = withContext(Dispatchers.IO) {
        Supabase.coneccion.postgrest.rpc(
            "crear_hospital",
            CrearHospitalRequest(
                p_codigo = codigo,
                p_nombre = nombre
            )
        )
    }
}
