package modelos

import database.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection

data class Patient(
    val id: Int,
    val clinicalRecordNumber: String,
    val name: String,
    val lastName: String,
    val birthDate: String,
    val address: String?,
    val unitName: String,
    val departmentName: String,
    val hospitalName: String
)

data class Doctor(
    val id: Int,
    val name: String,
    val lastName: String,
    val specialty: String,
    val licenseNumber: String,
    val phone: String?,
    val experience: Int,
    val contactInfo: String?,
    val unitName: String,
    val departmentName: String,
    val hospitalName: String
)

data class Hospital(
    val id: Int,
    val name: String
)
object Entities {
    suspend fun getDoctors(offset: Int, limit: Int): List<Doctor> = withContext(Dispatchers.IO) {
        val doctors = mutableListOf<Doctor>()
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "SELECT * FROM obtenerListadoMedicos() OFFSET ? LIMIT ?"
        )
        statement.setInt(1, offset)
        statement.setInt(2, limit)
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            doctors.add(
                Doctor(
                    id = resultSet.getInt("id"),
                    name = resultSet.getString("nombre"),
                    lastName = resultSet.getString("apellidos"),
                    specialty = resultSet.getString("especialidad"),
                    licenseNumber = resultSet.getString("numero_licencia_medica"),
                    phone = resultSet.getString("telefono"),
                    experience = resultSet.getInt("anios_experiencia"),
                    contactInfo = resultSet.getString("datos_contacto"),
                    unitName = resultSet.getString("nombre_unidad"),
                    departmentName = resultSet.getString("nombre_departamento"),
                    hospitalName = resultSet.getString("nombre_hospital")
                )
            )
        }
        resultSet.close()
        statement.close()
        connection.close()
        doctors
    }

    suspend fun getHospitals(offset: Int, limit: Int): List<Hospital> = withContext(Dispatchers.IO) {
        val hospitals = mutableListOf<Hospital>()
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "SELECT * FROM obtenerListadoHospitales() OFFSET ? LIMIT ?"
        )
        statement.setInt(1, offset)
        statement.setInt(2, limit)
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            hospitals.add(
                Hospital(
                    id = resultSet.getInt("id"),
                    name = resultSet.getString("nombre")
                )
            )
        }
        resultSet.close()
        statement.close()
        connection.close()
        hospitals
    }

    suspend fun getPatients(offset: Int, limit: Int): List<Patient> = withContext(Dispatchers.IO) {
        val patients = mutableListOf<Patient>()
        val connection: Connection = Database.getConnection()
        val statement = connection.prepareStatement(
            "SELECT * FROM obtenerListadoPacientes() OFFSET ? LIMIT ?"
        )
        statement.setInt(1, offset)
        statement.setInt(2, limit)
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            patients.add(
                Patient(
                    id = resultSet.getInt("id"),
                    clinicalRecordNumber = resultSet.getString("numero_historia_clinica"),
                    name = resultSet.getString("nombre"),
                    lastName = resultSet.getString("apellidos"),
                    birthDate = resultSet.getDate("fecha_nacimiento").toString(),
                    address = resultSet.getString("direccion_particular"),
                    unitName = resultSet.getString("nombre_unidad"),
                    departmentName = resultSet.getString("nombre_departamento"),
                    hospitalName = resultSet.getString("nombre_hospital")
                )
            )
        }
        resultSet.close()
        statement.close()
        connection.close()
        patients
    }
}