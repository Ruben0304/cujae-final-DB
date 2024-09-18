package vistas

import DialogButton
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import auth.Auth
import dao.DAOs
import dao.DoctorDAO
import global.Global
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import modelos.Doctor
import vistas.colores.doctorGradient
import vistas.colores.textColor
import vistas.componentes.AceptCancelDialogManager
import vistas.componentes.EditDialogManager
import vistas.componentes.InfoItem
import vistas.componentes.RotatingCard
import vistas.nav.NavManager


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
            if (Auth.hospital != "")
                if (unidadCodigo != null && departamentoId != null)
                    doctors = DoctorDAO.listar_medicos_por_unidad(
                        unidadCodigo, departamentoId,
                        Auth.hospital
                    )
                else if (departamentoId != null)
                    doctors = DoctorDAO.listar_medicos_por_departamento(departamentoId, Auth.hospital)
                else
                    doctors = DoctorDAO.listar_medicos_por_hospital(Auth.hospital)
            isLoading = false
        }
    }

    // Filtrar doctores
    var filteredDoctors = doctors.filter { doctor ->
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
            color = textColor
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

                itemsIndexed(filteredDoctors) { index, doctor ->
                    var visible by remember { mutableStateOf(false) }
                    val alpha by animateFloatAsState(
                        targetValue = if (visible) 1f else 0f,
                        animationSpec = tween(durationMillis = 1000)
                    )

                    LaunchedEffect(key1 = true) {
                        delay(150L * index) // Retraso escalonado para cada card
                        visible = true
                    }

                    Box(modifier = Modifier.alpha(alpha)) {
                        RotatingCard(
                            frontGradient = doctorGradient,
                            labelText = "Doctor",
                            avatar = painterResource("a.jpg"),
                            titleText = "${doctor.nombre} ${doctor.apellidos}",
                            subtitleText = doctor.especialidad,
                            infoItems = listOf(
                                InfoItem(Icons.Rounded.Badge, "Número de Historia", doctor.numeroLicencia),
                                InfoItem(Icons.Rounded.Phone, "Teléfono", doctor.telefono),
                                InfoItem(
                                    Icons.Rounded.CalendarToday,
                                    "Años de exp.",
                                    doctor.aniosExperiencia.toString()
                                ),
                                InfoItem(Icons.Rounded.Email, "Contacto", doctor.datosContacto)
                            )
                        ) {
                            GlassmorphismDialogManager.showDialog(
                                listOf(
                                    DialogButton("Crear cuenta", "👨🏼‍💻") {
                                        println("Aceptar clicked")
                                        GlassmorphismDialogManager.hideDialog()
                                    },
                                    DialogButton("Editar", "✍️") {
                                        GlassmorphismDialogManager.hideDialog()
                                        EditDialogManager.showDialog(
                                            textoP = "Editar Médico",
                                            acceptActionP = { updatedValues ->
                                                updatedValues.forEach(::println)
                                                coroutineScope.launch {
                                                    try {
                                                        DAOs.actualizarMedico(
                                                            codigo = doctor.codigo,
                                                            telefono = updatedValues["telefono"] ?: "",
                                                            aniosExperiencia = updatedValues["aniosExperiencia"]?.toIntOrNull()
                                                                ?: 0,
                                                            datosContacto = updatedValues["datosContacto"]
                                                                ?: "",
                                                            unidadCodigo = updatedValues["unidadCodigo"] ?: "",
                                                            departamentoCodigo = updatedValues["departamentoCodigo"]
                                                                ?: "",
                                                            hospitalCodigo = updatedValues["hospitalCodigo"]
                                                                ?: ""

                                                        )
                                                    }catch (e:Exception){
                                                        println(e.message)
                                                    }


                                                    NavManager.navController.navigate("medicos")
                                                    println("Deberia estar ok")
                                                }
                                            },
                                            entidadP = "Medico",
                                            initialValuesP = mapOf(
                                                "telefono" to doctor.telefono,
                                                "aniosExperiencia" to doctor.aniosExperiencia.toString(),
                                                "datosContacto" to doctor.datosContacto,
                                                "unidadCodigo" to doctor.unidad,
                                                "departamentoCodigo" to doctor.departamento,
                                                "hospitalCodigo" to doctor.hospital
                                            )
                                        )


                                    },
                                    DialogButton("Eliminar", "❗") {
                                        AceptCancelDialogManager.showDialog(
                                            "Seguro que deseas eliminar este médico ?",
                                            {
                                                coroutineScope.launch {
                                                    DoctorDAO.eliminarMedico(doctor.numeroLicencia); NavManager.navController.navigate(
                                                    "medicos"
                                                )
                                                }
                                            })
                                        GlassmorphismDialogManager.hideDialog()
                                    },
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}




