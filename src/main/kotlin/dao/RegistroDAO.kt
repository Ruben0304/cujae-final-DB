package dao

import dao.DoctorDAO.CrearMedicoRequest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import supabase.Supabase
import vistas.componentes.ToastManager
import vistas.componentes.ToastType


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
): String {
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
        return "Correcto"
    } catch (e: Exception) {
        return e.message.toString()
    }

}


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
)= withContext(Dispatchers.IO){
    try {
        Supabase.coneccion.postgrest.rpc(
            "marcar_paciente_no_atendido",
            CERNAequest(
                p_registro_id,
                p_unidad_codigo,
                p_departamento_codigo,
                p_hospital_codigo,
                p_causa_no_atencion
            )
        )
        ToastManager.showToast("Correcto",ToastType.SUCCESS)
    } catch (e: Exception) {
        ToastManager.showToast(e.message.toString(),ToastType.ERROR)

    }

}


