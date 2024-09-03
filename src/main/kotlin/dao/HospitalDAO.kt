package dao

import database.Database
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Hospital
import repository.Supabase
import java.sql.Connection
import java.sql.SQLException

object HospitalDAO {

    suspend fun getAllHospitals() : List<Hospital> = withContext(Dispatchers.IO) {
        Supabase.coneccion.from("hospital").select().decodeList<Hospital>()
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
