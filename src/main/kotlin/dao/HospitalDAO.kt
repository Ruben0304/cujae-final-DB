package dao

import database.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import modelos.Hospital
import java.sql.Connection
import java.sql.SQLException

object HospitalDAO {

    suspend fun getAllHospitals(): List<Hospital> = withContext(Dispatchers.IO) {
        val hospitals = mutableListOf<Hospital>()
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "SELECT * FROM Hospital"
        )
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            hospitals.add(
                Hospital(
                    codigo = resultSet.getString("codigo"),
                    nombre = resultSet.getString("nombre")
                )
            )
        }
        resultSet.close()
        statement.close()
        connection.close()
        hospitals
    }

    suspend fun getHospitalByCodigo(codigo: String): Hospital? = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "SELECT * FROM Hospital WHERE codigo = ?"
        )
        statement.setString(1, codigo)
        val resultSet = statement.executeQuery()

        val hospital = if (resultSet.next()) {
            Hospital(
                codigo = resultSet.getString("codigo"),
                nombre = resultSet.getString("nombre")
            )
        } else {
            null
        }
        resultSet.close()
        statement.close()
        connection.close()
        hospital
    }

    suspend fun createHospital(hospital: Hospital): Boolean = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "INSERT INTO Hospital (codigo, nombre) VALUES (?, ?)"
        )
        statement.setString(1, hospital.codigo)
        statement.setString(2, hospital.nombre)
        val rowsInserted = statement.executeUpdate()
        statement.close()
        connection.close()
        rowsInserted > 0
    }

    suspend fun updateHospital(hospital: Hospital): Boolean = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "UPDATE Hospital SET nombre = ? WHERE codigo = ?"
        )
        statement.setString(1, hospital.nombre)
        statement.setString(2, hospital.codigo)
        val rowsUpdated = statement.executeUpdate()
        statement.close()
        connection.close()
        rowsUpdated > 0
    }

    suspend fun deleteHospital(codigo: String): Boolean = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "DELETE FROM Hospital WHERE codigo = ?"
        )
        statement.setString(1, codigo)
        val rowsDeleted = statement.executeUpdate()
        statement.close()
        connection.close()
        rowsDeleted > 0
    }
}
