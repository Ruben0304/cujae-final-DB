import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditConsultaForm(initialValues: Map<String, String>): Map<String, String> {
    // Parsear la fecha y hora inicial desde el string recibido
    val initialDateTime = remember {
        LocalDateTime.parse(initialValues["fechaHora"], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
    var fecha by remember { mutableStateOf(initialDateTime.toLocalDate()) }
    var hora by remember { mutableStateOf(initialDateTime.toLocalTime()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fecha.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        initialDisplayMode = DisplayMode.Input

    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Mostrar fecha y hora seleccionadas en formato amigable
        Text(text = "Fecha: ${fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
        Text(text = "Hora: ${hora.format(DateTimeFormatter.ofPattern("HH:mm"))}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth(.4f),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Seleccionar fecha", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showTimePicker = true },
            modifier = Modifier.fillMaxWidth(.4f),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Seleccionar hora", color = Color.White)
        }

        Spacer(modifier = Modifier.height(100.dp))
    }

    // Mostrar DatePicker si está habilitado
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        fecha = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Mostrar TimePicker si está habilitado
    if (showTimePicker) {
        val timePickerState = remember { TimePickerState(initialHour = hora.hour, initialMinute = hora.minute, true) }

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    hora = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    // Devolver el valor de la fecha y hora formateada para enviar
    return mapOf(
        // Sumar un día a la fecha seleccionada antes de formatear
        "fechaHora" to LocalDateTime.of(fecha, hora).plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

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

    var direccion by remember { mutableStateOf(initialValues["direccion"] ?: "") }
    var nombre by remember { mutableStateOf(initialValues["nombre"] ?: "") }
    var apellidos by remember { mutableStateOf(initialValues["apellidos"] ?: "") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        TextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección ") })
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") })
    }

    return mapOf(
        "direccion" to direccion,
        "nombre" to nombre,
        "apellidos" to apellidos
    )
}

@Composable
fun EditUnidadForm(initialValues: Map<String, String>): Map<String, String> {

    var nombre by remember { mutableStateOf(initialValues["nombre"] ?: "") }
    var ubicacion by remember { mutableStateOf(initialValues["ubicacion"] ?: "") }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación") })

    }

    return mapOf(
        "nombre" to nombre,
        "ubicacion" to ubicacion,
    )
}