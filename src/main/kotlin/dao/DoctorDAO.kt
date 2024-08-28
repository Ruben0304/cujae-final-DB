package dao

import database.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import modelos.Doctor
import java.sql.Connection

object DoctorDAO {

    suspend fun getFilteredDoctors(
        hospitalId: String?,
        departamentoId: String? = null,
        unidadId: String? = null,
        offset: Int,
        limit: Int
    ): List<Doctor> = withContext(Dispatchers.IO) {
        val doctors = mutableListOf<Doctor>()
        val connection: Connection = Database.getConnection()

        val query = when {
            hospitalId == null -> "SELECT * FROM medico OFFSET ? LIMIT ?"
            unidadId != null -> "SELECT * FROM get_doctors_by_unit(?, ?, ?) OFFSET ? LIMIT ?"
            departamentoId != null -> "SELECT * FROM get_doctors_by_department(?, ?) OFFSET ? LIMIT ?"
            else -> "SELECT * FROM listar_medicos_por_hospital(?) OFFSET ? LIMIT ?"
        }

        val statement = connection.prepareStatement(query)

        when {
            hospitalId == null ->{
                statement.setInt(1, offset)
                statement.setInt(2, limit)
            }
            unidadId != null -> {
                statement.setString(1, hospitalId)
                statement.setString(2, departamentoId!!)
                statement.setString(3, unidadId)
                statement.setInt(4, offset)
                statement.setInt(5, limit)
            }
            departamentoId != null -> {
                statement.setString(1, hospitalId)
                statement.setString(2, departamentoId)
                statement.setInt(3, offset)
                statement.setInt(4, limit)
            }
            else -> {
                statement.setString(1, hospitalId)
                statement.setInt(2, offset)
                statement.setInt(3, limit)
            }
        }

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            doctors.add(
                Doctor(
                    nombre = resultSet.getString("nombre"),
                    apellidos = resultSet.getString("apellidos"),
                    especialidad = resultSet.getString("especialidad"),
                    numeroLicencia = resultSet.getString("numero_licencia"),
                    telefono = resultSet.getString("telefono"),
                    aniosExperiencia = resultSet.getObject("anios_experiencia") as Int?,
                    datosContacto = resultSet.getString("datos_contacto"),
                )
            )
        }
        resultSet.close()
        statement.close()
        connection.close()
        doctors
    }

    suspend fun getDoctorByCodigo(codigo: String): Doctor? = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "SELECT * FROM Medico WHERE codigo = ?"
        )
        statement.setString(1, codigo)
        val resultSet = statement.executeQuery()

        val doctor = if (resultSet.next()) {
            Doctor(
                nombre = resultSet.getString("nombre"),
                apellidos = resultSet.getString("apellidos"),
                especialidad = resultSet.getString("especialidad"),
                numeroLicencia = resultSet.getString("numero_licencia"),
                telefono = resultSet.getString("telefono"),
                aniosExperiencia = resultSet.getObject("anios_experiencia") as Int?,
                datosContacto = resultSet.getString("datos_contacto"),
            )
        } else {
            null
        }
        resultSet.close()
        statement.close()
        connection.close()
        doctor
    }

    suspend fun createDoctor(doctor: Doctor): Boolean = withContext(Dispatchers.IO) {
        val connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "INSERT INTO Medico (codigo, nombre, apellidos, especialidad, numero_licencia, telefono, anios_experiencia, datos_contacto, unidad_codigo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        )

        statement.setString(2, doctor.nombre)
        statement.setString(3, doctor.apellidos)
        statement.setString(4, doctor.especialidad)
        statement.setString(5, doctor.numeroLicencia)
        statement.setString(6, doctor.telefono)
        statement.setObject(7, doctor.aniosExperiencia)
        statement.setString(8, doctor.datosContacto)
        val rowsInserted = statement.executeUpdate()
        statement.close()
        connection.close()
        rowsInserted > 0
    }

    suspend fun updateDoctor(doctor: Doctor): Boolean = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "UPDATE Medico SET nombre = ?, apellidos = ?, especialidad = ?, numero_licencia = ?, telefono = ?, anios_experiencia = ?, datos_contacto = ?, unidad_codigo = ? WHERE codigo = ?"
        )
        statement.setString(1, doctor.nombre)
        statement.setString(2, doctor.apellidos)
        statement.setString(3, doctor.especialidad)
        statement.setString(4, doctor.numeroLicencia)
        statement.setString(5, doctor.telefono)
        statement.setObject(6, doctor.aniosExperiencia)
        statement.setString(7, doctor.datosContacto)

        val rowsUpdated = statement.executeUpdate()
        statement.close()
        connection.close()
        rowsUpdated > 0
    }

    suspend fun deleteDoctor(codigo: String): Boolean = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "DELETE FROM Medico WHERE codigo = ?"
        )
        statement.setString(1, codigo)
        val rowsDeleted = statement.executeUpdate()
        statement.close()
        connection.close()
        rowsDeleted > 0
    }
}
