package dao


import dao.PatientDAO.OPCSRequest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import modelos.Registro
import supabase.Supabase
import vistas.componentes.ToastManager
import vistas.componentes.ToastType

object RegistroDAO {

    @Serializable
    data class CERequest(
        val p_registro_id: Int,
        val p_unidad_codigo: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String,
        val p_nuevo_estado: String
    )

    suspend fun cambiarEstadoEnRegistro(
        p_registro_id: Int,
        p_unidad_codigo: String,
        p_departamento_codigo: String,
        p_hospital_codigo: String,
        p_nuevo_estado: String
    ) = withContext(Dispatchers.IO) {

        try {
            Supabase.coneccion.postgrest.rpc(
                "modificar_estado_paciente",
                CERequest(
                    p_registro_id,
                    p_unidad_codigo,
                    p_departamento_codigo,
                    p_hospital_codigo,
                    p_nuevo_estado
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    @Serializable
    data class CERNAequest(
        val p_registro_id: Int,
        val p_unidad_codigo: String,
        val p_departamento_codigo: String,
        val p_hospital_codigo: String,
        val p_causa_no_atencion: String
    )

    suspend fun marcarNoAtendido(
        p_registro_id: Int,
        p_unidad_codigo: String,
        p_departamento_codigo: String,
        p_hospital_codigo: String,
        p_causa_no_atencion: String
    ) = withContext(Dispatchers.IO) {
        val c = CERNAequest(
            p_registro_id,
            p_unidad_codigo,
            p_departamento_codigo,
            p_hospital_codigo,
            p_causa_no_atencion
        )
        try {


            Supabase.coneccion.postgrest.rpc(
                "marcar_paciente_no_atendido_con_causa",c

            )
            println(c)
        } catch (e: Exception) {
            println(c)
            e.printStackTrace()

        }

    }

}
