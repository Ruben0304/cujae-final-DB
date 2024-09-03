package vistas.form


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dao.TurnoDAO
import kotlinx.coroutines.launch
import vistas.componentes.SubmitButton
import vistas.componentes.TextInputField
import java.time.LocalDate

@Composable
fun CreateTurnoForm() {
    var numeroTurno by remember { mutableStateOf("") }
    var unidadCodigo by remember { mutableStateOf("") }
    var departamentoCodigo by remember { mutableStateOf("") }
    var hospitalCodigo by remember { mutableStateOf("") }
    var medicoCodigo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var pacientesAtendidos by remember { mutableStateOf("") }
    var pacientesAsignados by remember { mutableStateOf("") }
    val corrutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            TextInputField(value = numeroTurno, onValueChange = { numeroTurno = it }, label = "Número de turno")
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
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = medicoCodigo, onValueChange = { medicoCodigo = it }, label = "Código de médico")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = fecha, onValueChange = { fecha = it }, label = "Fecha")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(
                value = pacientesAtendidos,
                onValueChange = { pacientesAtendidos = it },
                label = "Pacientes atendidos"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(
                value = pacientesAsignados,
                onValueChange = { pacientesAsignados = it },
                label = "Pacientes asignados"
            )
            Spacer(modifier = Modifier.height(24.dp))
            SubmitButton(
                onClicked = {
                    corrutineScope.launch {
                        TurnoDAO.crearTurno(
                            numeroTurno.toInt(),
                            unidadCodigo,
                            departamentoCodigo,
                            hospitalCodigo,
                            medicoCodigo,
                            LocalDate.parse(fecha).toString(),
                            pacientesAtendidos.toInt(),
                            pacientesAsignados.toInt()
                        )
                    }
                }
            )
        }
    }
}