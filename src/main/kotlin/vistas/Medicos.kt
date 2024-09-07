package vistas

import DialogButton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dao.DoctorDAO
import global.Global
import kotlinx.coroutines.launch
import modelos.Doctor
import vistas.colores.doctorGradient
import vistas.componentes.InfoItem
import vistas.componentes.RotatingCard


@Composable
fun DoctorListContent(departamentoId: String? = null, unidadCodigo: String? = null) {
    var doctors by remember { mutableStateOf(listOf<Doctor>()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedHospital by remember { mutableStateOf<String?>(null) }
    var selectedDepartamento by remember { mutableStateOf<String?>(null) }
    var selectedUnidad by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Simular carga de doctores
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            if (Global.selectedHospital != null)
                if (unidadCodigo != null && departamentoId != null)
                    doctors = DoctorDAO.listar_medicos_por_unidad(unidadCodigo, departamentoId,
                        Global.selectedHospital!!
                    )
                else if (departamentoId != null)
                    doctors = DoctorDAO.listar_medicos_por_departamento(departamentoId, Global.selectedHospital!!)
                else
                    doctors = DoctorDAO.listar_medicos_por_hospital(Global.selectedHospital!!)
            isLoading = false
        }
    }

    // Filtrar doctores









































































    val filteredDoctors = doctors.filter { doctor ->
        (selectedHospital == null || doctor.apellidos == selectedHospital) &&
                (selectedDepartamento == null || doctor.especialidad == selectedDepartamento) &&
                (selectedUnidad == null || doctor.telefono == selectedUnidad)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Listado de médicos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = Color.White
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(16, 78, 146))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 190.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredDoctors) { doctor ->
                    RotatingCard(
                        frontGradient = doctorGradient,
                        labelText = "Doctor",
                        avatar = painterResource("a.jpg"),
                        titleText = doctor.nombre + " " + doctor.apellidos,
                        subtitleText = doctor.especialidad,
                        infoItems = listOf(
                            InfoItem(Icons.Rounded.Badge, "Número de Historia", doctor.numeroLicencia),
                            InfoItem(Icons.Rounded.Phone, "Teléfono", doctor.telefono),
                            InfoItem(Icons.Rounded.CalendarToday, "Años de exp.", doctor.aniosExperiencia.toString()),
                            InfoItem(Icons.Rounded.Email, "Contacto", doctor.datosContacto)
                        )
                    ){
                        GlassmorphismDialogManager.showDialog(
                            listOf(
                                DialogButton(
                                    "Crear cuenta",
                                    "✅"
                                ) { println("Aceptar clicked"); GlassmorphismDialogManager.hideDialog() },
                                DialogButton(
                                    "Editar",
                                    "❌"
                                ) { println("Cancelar clicked"); GlassmorphismDialogManager.hideDialog() },
                                DialogButton("Más información", "ℹ️") { println("Más información clicked") }
                            )
                        )
                    }
                }
            }
        }
    }
}




