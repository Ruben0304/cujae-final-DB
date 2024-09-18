package dao

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import modelos.Informe
import supabase.Supabase

object InformesDAO {

    suspend fun obtenerInformes(u:String, d:String, h:String) = withContext(Dispatchers.IO) {
        val columns = Columns.raw("""
            numero_informe,
            unidad_codigo,
            departamento_codigo,
            hospital_codigo,
            fecha_hora,
            pacientes_atendidos,
            pacientes_alta,
            pacientes_admitidos,
            total_registro
        """.trimIndent())

        try {
            Supabase.coneccion
                .from("informe")
                .select(columns = columns){
                    filter {
                        and {
                            eq("unidad_codigo", u)
                            eq("departamento_codigo", d)
                            eq("hospital_codigo", h)
                        }
                    }
                }
                .decodeList<Informe>()
        }catch (e: Exception){
            println(e.message)
            e.printStackTrace()
            emptyList()
        }

    }
}
