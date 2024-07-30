package dao

import database.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import modelos.Departamento
import java.sql.Connection


object DepartamentoDAO {
    suspend fun obtenerDepartamentosPorHospital(hospitalCodigo: String): List<Departamento> = withContext(Dispatchers.IO) {
        val departamentos = mutableListOf<Departamento>()
        val connection: Connection = Database.getConnection()

        val query = "SELECT * FROM obtener_departamentos_por_hospital(?)"

        connection.prepareStatement(query).use { statement ->
            statement.setString(1, hospitalCodigo)
            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                departamentos.add(
                    Departamento(
                        codigo = resultSet.getString("departamento_codigo"),
                        nombre = resultSet.getString("departamento_nombre"),
                        hospitalCodigo = hospitalCodigo
                    )
                )
            }
        }

        connection.close()
        departamentos
    }
}