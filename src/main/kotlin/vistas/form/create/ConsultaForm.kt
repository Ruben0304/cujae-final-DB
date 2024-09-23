package vistas.form.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import auth.Auth
import dao.ConsultaDAO
import dao.PatientDAO
import kotlinx.coroutines.launch
import modelos.ConsultaRequest
import modelos.PatientRequest
import vistas.componentes.SubmitButton
import vistas.nav.NavManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateConsultaForm() {
    var fecha by remember { mutableStateOf(LocalDateTime.now().toLocalDate()) } // Solo la fecha
    var hora by remember { mutableStateOf(LocalDateTime.now().toLocalTime()) } // Solo la hora
    var pacienteCI by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var diaNacimiento by remember { mutableStateOf("") }
    var mesNacimiento by remember { mutableStateOf("") }
    var anioNacimiento by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showAdditionalFields by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) } // Añadido para el TimePicker

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    val coroutineScope = rememberCoroutineScope()
    val customBlue = Color(0xFF4285F4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.height(300.dp)) {
            item {
                OutlinedTextField(
                    value = pacienteCI,
                    onValueChange = {
                        if (it.length <= 11) {
                            pacienteCI = it
                            if (it.length == 11) {
                                isLoading = true
                                coroutineScope.launch {
                                    val result = PatientDAO.comprobarPaciente(it)
                                    showAdditionalFields = !result
                                    isLoading = false
                                }
                            } else {
                                showAdditionalFields = false
                            }
                        }
                    },
                    label = { Text("Carnet del paciente") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (showAdditionalFields) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del paciente") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it },
                        label = { Text("Apellido del paciente") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = diaNacimiento,
                            onValueChange = { diaNacimiento = it },
                            label = { Text("Día") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = mesNacimiento,
                            onValueChange = { mesNacimiento = it },
                            label = { Text("Mes") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = anioNacimiento,
                            onValueChange = { anioNacimiento = it },
                            label = { Text("Año") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección del paciente") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(.4f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Seleccionar fecha", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showTimePicker = true }, // Botón para seleccionar hora
                    modifier = Modifier.fillMaxWidth(.4f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Seleccionar hora", color = Color.White)
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        SubmitButton {
            coroutineScope.launch {
                var paciente = true
                if (showAdditionalFields) {
                    val fechaNacimiento = kotlinx.datetime.LocalDate(
                        anioNacimiento.toInt(),
                        mesNacimiento.toInt(),
                        diaNacimiento.toInt()
                    )
                    val patient = PatientRequest(
                        ci = pacienteCI,
                        nombre = nombre,
                        apellidos = apellido,
                        fecha_nacimiento = fechaNacimiento,
                        direccion = direccion
                    )
                    paciente = PatientDAO.insert(patient) != null
                }


                // Combinar la fecha y la hora seleccionadas para obtener un `LocalDateTime`
                val fechaCompleta = LocalDateTime.of(fecha, hora)

// Sumar 1 día a la fecha completa
                val fechaCompletaMasUnDia = fechaCompleta.plusDays(1)

// Formatear la fecha completa en el formato deseado
                val fechaHoraFormateada =
                    fechaCompletaMasUnDia.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                try {
                    ConsultaDAO.crearConsultaConRegistro(
                        ConsultaRequest(
                            codigo = Auth.idMedico!!,          // Usar el código del médico
                            timestamp = fechaHoraFormateada,   // Fecha y hora formateadas
                            registro = pacienteCI              // Carnet del paciente
                        )
                    )
                    println("Debe estar empingao")
                    NavManager.refresh() // Actualizar la interfaz o navegación
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // Llamar al DAO con la fecha y hora formateadas


            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        fecha = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                    showTimePicker = true // Mostrar TimePicker después de seleccionar la fecha
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

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        fecha = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                    showTimePicker = true // Mostrar TimePicker después de seleccionar la fecha
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

    if (showTimePicker) {
        val timePickerState = remember { TimePickerState(initialHour = hora.hour, initialMinute = hora.minute, true) }

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Obtener la hora y los minutos seleccionados
                    hora = LocalDateTime.of(fecha, hora).toLocalTime().withHour(timePickerState.hour)
                        .withMinute(timePickerState.minute)
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

}
