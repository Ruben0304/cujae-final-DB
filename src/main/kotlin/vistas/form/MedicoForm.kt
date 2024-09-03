package vistas.form


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dao.DoctorDAO
import kotlinx.coroutines.launch
import vistas.componentes.SubmitButton
import vistas.componentes.TextAreaField
import vistas.componentes.TextInputField


@Composable
fun CreateMedicoForm() {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var numeroLicencia by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var aniosExperiencia by remember { mutableStateOf("") }
    var datosContacto by remember { mutableStateOf("") }
    var unidadCodigo by remember { mutableStateOf("") }
    var departamentoCodigo by remember { mutableStateOf("") }
    var hospitalCodigo by remember { mutableStateOf("") }
    val corrutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            TextInputField(value = codigo, onValueChange = { codigo = it }, label = "Código de médico")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = nombre, onValueChange = { nombre = it }, label = "Nombre")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = apellidos, onValueChange = { apellidos = it }, label = "Apellidos")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = especialidad, onValueChange = { especialidad = it }, label = "Especialidad")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(
                value = numeroLicencia,
                onValueChange = { numeroLicencia = it },
                label = "Número de licencia"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = telefono, onValueChange = { telefono = it }, label = "Teléfono")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(
                value = aniosExperiencia,
                onValueChange = { aniosExperiencia = it },
                label = "Años de experiencia"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextAreaField(value = datosContacto, onValueChange = { datosContacto = it }, label = "Datos de contacto")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = unidadCodigo, onValueChange = { unidadCodigo = it }, label = "Código de unidad")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(
                value = departamentoCodigo,
                onValueChange = { departamentoCodigo = it },
                label = "Código de departamento"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(
                value = hospitalCodigo,
                onValueChange = { hospitalCodigo = it },
                label = "Código de hospital"
            )
            Spacer(modifier = Modifier.height(24.dp))
            SubmitButton(

                onClicked = {
                    corrutineScope.launch {
                        DoctorDAO.crearMedico(
                            codigo,
                            nombre,
                            apellidos,
                            especialidad,
                            numeroLicencia,
                            telefono,
                            aniosExperiencia.toInt(),
                            datosContacto,
                            unidadCodigo,
                            departamentoCodigo,
                            hospitalCodigo
                        )
                    }
                }
            )
        }
    }
}