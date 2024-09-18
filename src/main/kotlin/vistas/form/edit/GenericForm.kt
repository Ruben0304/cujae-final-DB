import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditConsultaForm(initialValues: Map<String, String>): Map<String, String> {


    var fechaHora by remember { mutableStateOf(initialValues["fechaHora"] ?: "") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        TextField(value = fechaHora, onValueChange = { fechaHora = it }, label = { Text("Fecha y Hora") })
    }

    return mapOf(
        "fechaHora" to fechaHora
    )
}

@Composable
fun EditDepartamentoForm(initialValues: Map<String, String>): Map<String, String> {

    var nombre by remember { mutableStateOf(initialValues["nombre"] ?: "") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
    }

    return mapOf(
        "nombre" to nombre
    )
}

@Composable
fun EditHospitalForm(initialValues: Map<String, String>): Map<String, String> {
    var codigo by remember { mutableStateOf(initialValues["codigo"] ?: "") }
    var nombre by remember { mutableStateOf(initialValues["nombre"] ?: "") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código") })
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
    }

    return mapOf(
        "codigo" to codigo,
        "nombre" to nombre
    )
}

@Composable
fun EditMedicoForm(initialValues: Map<String, String>): Map<String, String> {
    var codigo by remember { mutableStateOf(initialValues["codigo"] ?: "") }
    var telefono by remember { mutableStateOf(initialValues["telefono"] ?: "") }
    var aniosExperiencia by remember { mutableStateOf(initialValues["aniosExperiencia"] ?: "") }
    var datosContacto by remember { mutableStateOf(initialValues["datosContacto"] ?: "") }
    var unidadCodigo by remember { mutableStateOf(initialValues["unidadCodigo"] ?: "") }
    var departamentoCodigo by remember { mutableStateOf(initialValues["departamentoCodigo"] ?: "") }
    var hospitalCodigo by remember { mutableStateOf(initialValues["hospitalCodigo"] ?: "") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código") })
        TextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
        TextField(value = aniosExperiencia, onValueChange = { aniosExperiencia = it }, label = { Text("Años de Experiencia") })
        TextField(value = datosContacto, onValueChange = { datosContacto = it }, label = { Text("Datos de Contacto") })
        TextField(value = unidadCodigo, onValueChange = { unidadCodigo = it }, label = { Text("Unidad Código") })
        TextField(value = departamentoCodigo, onValueChange = { departamentoCodigo = it }, label = { Text("Departamento Código") })
        TextField(value = hospitalCodigo, onValueChange = { hospitalCodigo = it }, label = { Text("Hospital Código") })
    }

    return mapOf(
        "codigo" to codigo,
        "telefono" to telefono,
        "aniosExperiencia" to aniosExperiencia,
        "datosContacto" to datosContacto,
        "unidadCodigo" to unidadCodigo,
        "departamentoCodigo" to departamentoCodigo,
        "hospitalCodigo" to hospitalCodigo
    )
}

@Composable
fun EditPacienteForm(initialValues: Map<String, String>): Map<String, String> {
    var registroId by remember { mutableStateOf(initialValues["registroId"] ?: "") }
    var unidadCodigo by remember { mutableStateOf(initialValues["unidadCodigo"] ?: "") }
    var departamentoCodigo by remember { mutableStateOf(initialValues["departamentoCodigo"] ?: "") }
    var hospitalCodigo by remember { mutableStateOf(initialValues["hospitalCodigo"] ?: "") }
    var nuevaDireccion by remember { mutableStateOf(initialValues["nuevaDireccion"] ?: "") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextField(value = registroId, onValueChange = { registroId = it }, label = { Text("Registro ID") })
        TextField(value = unidadCodigo, onValueChange = { unidadCodigo = it }, label = { Text("Unidad Código") })
        TextField(value = departamentoCodigo, onValueChange = { departamentoCodigo = it }, label = { Text("Departamento Código") })
        TextField(value = hospitalCodigo, onValueChange = { hospitalCodigo = it }, label = { Text("Hospital Código") })
        TextField(value = nuevaDireccion, onValueChange = { nuevaDireccion = it }, label = { Text("Nueva Dirección") })
    }

    return mapOf(
        "registroId" to registroId,
        "unidadCodigo" to unidadCodigo,
        "departamentoCodigo" to departamentoCodigo,
        "hospitalCodigo" to hospitalCodigo,
        "nuevaDireccion" to nuevaDireccion
    )
}

@Composable
fun EditUnidadForm(initialValues: Map<String, String>): Map<String, String> {
    var codigo by remember { mutableStateOf(initialValues["codigo"] ?: "") }
    var nombre by remember { mutableStateOf(initialValues["nombre"] ?: "") }
    var ubicacion by remember { mutableStateOf(initialValues["ubicacion"] ?: "") }
    var departamentoCodigo by remember { mutableStateOf(initialValues["departamentoCodigo"] ?: "") }
    var hospitalCodigo by remember { mutableStateOf(initialValues["hospitalCodigo"] ?: "") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código") })
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación") })
        TextField(value = departamentoCodigo, onValueChange = { departamentoCodigo = it }, label = { Text("Departamento Código") })
        TextField(value = hospitalCodigo, onValueChange = { hospitalCodigo = it }, label = { Text("Hospital Código") })
    }

    return mapOf(
        "codigo" to codigo,
        "nombre" to nombre,
        "ubicacion" to ubicacion,
        "departamentoCodigo" to departamentoCodigo,
        "hospitalCodigo" to hospitalCodigo
    )
}