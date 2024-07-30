package modelos

data class Doctor(
    val nombre: String,
    val apellidos: String,
    val especialidad: String,
    val numeroLicencia: String,
    val telefono: String?,
    val aniosExperiencia: Int?,
    val datosContacto: String?,
)
