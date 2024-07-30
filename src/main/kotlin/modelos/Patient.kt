package modelos

data class Patient(
    val numeroHistoriaClinica: String,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String,
    val direccion: String,
    val unidadNombre: String,
    val departamentoNombre: String
)

