package vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import vistas.componentes.*
import vistas.form.*


@Composable
fun CreateFormScreen() {
    var selectedOption by remember { mutableStateOf("Consulta") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxSize(.9f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Crear Nuevo Registro",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFFFFFFF),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    CustomDropdown(
                        options = listOf("Consulta", "Departamento", "Hospital", "Medico", "Paciente", "Turno", "Unidad"),
                        selectedOption = selectedOption,
                        onOptionSelected = { selectedOption = it }
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                // Muestra el formulario correspondiente
                when (selectedOption) {
                    "Consulta" -> CreateConsultaForm()
                    "Departamento" -> CreateDepartamentoForm()
                    "Hospital" -> CreateHospitalForm()
                    "Medico" -> CreateMedicoForm()
                    "Paciente" -> CreatePacienteForm()
                    "Turno" -> CreateTurnoForm()
                    "Unidad" -> CreateUnidadForm()
                    // Agrega más casos si es necesario
                }
            }
        }
    }
}



