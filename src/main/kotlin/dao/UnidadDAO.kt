package dao

import database.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import modelos.Unidad
import java.sql.Connection



object UnidadDAO {
    suspend fun obtenerUnidadesPorHospitalYDepartamento(
        hospitalCodigo: String,
        departamentoCodigo: String,
        offset: Int,
        limit: Int
    ): List<Unidad> = withContext(Dispatchers.IO) {
        val unidades = mutableListOf<Unidad>()
        val connection: Connection = Database.getConnection()

        val query = "SELECT * FROM obtener_unidades_por_hospital_y_departamento(?, ?) OFFSET ? LIMIT ?"

        connection.prepareStatement(query).use { statement ->
            statement.setString(1, hospitalCodigo)
            statement.setString(2, departamentoCodigo)
            statement.setInt(3, offset)
            statement.setInt(4, limit)
            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                unidades.add(
                    Unidad(
                        codigo = resultSet.getString("unidad_codigo"),
                        nombre = resultSet.getString("unidad_nombre"),
                        ubicacion = resultSet.getString("ubicacion"),
                        departamentoCodigo = departamentoCodigo,
                        hospitalCodigo = hospitalCodigo
                    )
                )
            }
        }

        connection.close()
        unidades
    }
}