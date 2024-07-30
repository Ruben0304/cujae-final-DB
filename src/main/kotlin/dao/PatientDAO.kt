package dao

import database.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import modelos.Patient
import java.sql.Connection

object PatientDAO {

    suspend fun getFilteredPatients(
        hospitalId: Int?,
        departamentoId: Int? = null,
        unidadId: Int? = null,
        offset: Int,
        limit: Int
    ): List<Patient> = withContext(Dispatchers.IO) {
        val patients = mutableListOf<Patient>()
        val connection: Connection = Database.getConnection()

        val query = when {
            hospitalId == null -> "SELECT * FROM listar_pacientes_todos() OFFSET ? LIMIT ?"
            unidadId != null -> "SELECT * FROM get_patients_by_unit(?, ?, ?) OFFSET ? LIMIT ?"
            departamentoId != null -> "SELECT * FROM get_patients_by_department(?, ?) OFFSET ? LIMIT ?"
            else -> "SELECT * FROM get_patients_by_hospital(?) OFFSET ? LIMIT ?"
        }

        val statement = connection.prepareStatement(query)

        when {
            hospitalId == null ->{
                statement.setInt(1, offset)
                statement.setInt(2, limit)
            }

            unidadId != null -> {
                statement.setInt(1, hospitalId)
                statement.setInt(2, departamentoId!!)
                statement.setInt(3, unidadId)
                statement.setInt(4, offset)
                statement.setInt(5, limit)
            }
            departamentoId != null -> {
                statement.setInt(1, hospitalId)
                statement.setInt(2, departamentoId)
                statement.setInt(3, offset)
                statement.setInt(4, limit)
            }
            else -> {
                statement.setInt(1, hospitalId)
                statement.setInt(2, offset)
                statement.setInt(3, limit)
            }
        }

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            patients.add(
                Patient(
                    numeroHistoriaClinica = resultSet.getString("numero_historia_clinica"),
                    nombre = resultSet.getString("paciente_nombre"),
                    apellidos = resultSet.getString("paciente_apellidos"),
                    fechaNacimiento = resultSet.getDate("fecha_nacimiento").toString(),
                    direccion = resultSet.getString("direccion"),
                    unidadNombre = resultSet.getString("unidad_nombre"),
                    departamentoNombre = resultSet.getString("departamento_nombre")
                )



            )
        }
        resultSet.close()
        statement.close()
        connection.close()
        patients
    }
    suspend fun getPatientByNumeroHistoriaClinica(numeroHistoriaClinica: String): Patient? = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "SELECT * FROM Paciente WHERE numero_historia_clinica = ?"
        )
        statement.setString(1, numeroHistoriaClinica)
        val resultSet = statement.executeQuery()

        val patient = if (resultSet.next()) {
            Patient(
                numeroHistoriaClinica = resultSet.getString("numero_historia_clinica"),
                nombre = resultSet.getString("paciente_nombre"),
                apellidos = resultSet.getString("paciente_apellidos"),
                fechaNacimiento = resultSet.getDate("fecha_nacimiento").toString(),
                direccion = resultSet.getString("direccion"),
                unidadNombre = resultSet.getString("unidad_nombre"),
                departamentoNombre = resultSet.getString("departamento_nombre")
            )
        } else {
            null
        }
        resultSet.close()
        statement.close()
        connection.close()
        patient
    }

    suspend fun createPatient(patient: Patient): Boolean = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "INSERT INTO Paciente (numero_historia_clinica, nombre, apellidos, fecha_nacimiento, direccion, unidad_codigo) VALUES (?, ?, ?, ?, ?, ?)"
        )
        statement.setString(1, patient.numeroHistoriaClinica)
        statement.setString(2, patient.nombre)
        statement.setString(3, patient.apellidos)
        statement.setDate(4, java.sql.Date.valueOf(patient.fechaNacimiento))
        statement.setString(5, patient.direccion)
        statement.setString(6, patient.unidadNombre)
        statement.setString(6, patient.departamentoNombre)
        val rowsInserted = statement.executeUpdate()
        statement.close()
        connection.close()
        rowsInserted > 0
    }

    suspend fun updatePatient(patient: Patient): Boolean = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "UPDATE Paciente SET nombre = ?, apellidos = ?, fecha_nacimiento = ?, direccion = ?, unidad_codigo = ? WHERE numero_historia_clinica = ?"
        )
        statement.setString(1, patient.nombre)
        statement.setString(2, patient.apellidos)
        statement.setDate(3, java.sql.Date.valueOf(patient.fechaNacimiento))
        statement.setString(4, patient.direccion)
        statement.setString(5, patient.unidadNombre)
        statement.setString(6, patient.numeroHistoriaClinica)
        val rowsUpdated = statement.executeUpdate()
        statement.close()
        connection.close()
        rowsUpdated > 0
    }

    suspend fun deletePatient(numeroHistoriaClinica: String): Boolean = withContext(Dispatchers.IO) {
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "DELETE FROM Paciente WHERE numero_historia_clinica = ?"
        )
        statement.setString(1, numeroHistoriaClinica)
        val rowsDeleted = statement.executeUpdate()
        statement.close()
        connection.close()
        rowsDeleted > 0
    }





}
