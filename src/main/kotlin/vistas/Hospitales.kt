import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import auth.Auth
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Hospital
import dao.DAOs
import dao.HospitalDAO
import kotlinx.coroutines.launch
import modelos.Hospital
import vistas.colores.hospitalGradient
import vistas.colores.textColor
import vistas.componentes.AceptCancelDialogManager
import vistas.componentes.EditDialogManager
import vistas.componentes.InfoItem
import vistas.componentes.RotatingCard
import vistas.nav.NavManager

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HospitalListContent() {
    var hospitales by remember { mutableStateOf(listOf<Hospital>()) }
    var isLoading by remember { mutableStateOf(true) }
    val corrutineScope = rememberCoroutineScope()


    // Simular carga de doctores
    LaunchedEffect(key1 = true) {


        hospitales = HospitalDAO.getAllHospitals()
        isLoading = false

    }



    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Listado de hospitales",
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
                items(hospitales) { hospital ->
                    RotatingCard(
                        frontGradient = hospitalGradient,
                        labelText = "Hospital",
                        avatar = painterResource("Hospital Sign.png"),
                        titleText = hospital.nombre,
                        subtitleText = "",
                        infoItems = listOf(
                            InfoItem(
                                FontAwesomeIcons.Solid.Hospital,
                                "Departamentos",
                                hospital.cantidadDepartamentos.toString()
                            ),
                            InfoItem(Icons.Default.People, "Unidades", hospital.cantidadUnidades.toString()),
                            InfoItem(Icons.Default.MedicalServices, "Médicos", hospital.cantidadMedicos.toString()),
                            InfoItem(Icons.Rounded.Sick, "Pacientes", hospital.cantidadPacientes.toString())
                        )
                    ) {
                        GlassmorphismDialogManager.showDialog(
                            listOf(
                                DialogButton(
                                    "Editar",
                                    "✍️"
                                ) {
                                    EditDialogManager.showDialog(
                                        textoP = "Editar Hospital",
                                        acceptActionP = { updatedValues ->
                                            updatedValues.forEach(::println)
                                            corrutineScope.launch {
                                                try {
                                                    DAOs.actualizarHospital(
                                                        codigo = hospital.codigo,
                                                        nombre = updatedValues["nombre"] ?: ""
                                                    )
                                                } catch (e: Exception) {
                                                    println(e.message)
                                                }
                                                NavManager.refresh()
                                            }
                                        },
                                        entidadP = "Hospital",
                                        initialValuesP = mapOf(
                                            "nombre" to hospital.nombre
                                        )
                                    ); GlassmorphismDialogManager.hideDialog()
                                },
                                DialogButton(
                                    "Eliminar",
                                    "❗"
                                ) {
                                    corrutineScope.launch {
                                        println("debe ok")
                                        HospitalDAO.eliminar(hospital.codigo)
                                        NavManager.refresh()
                                        GlassmorphismDialogManager.hideDialog()

                                    }

                                }
                            ))

                    }
                }
            }
        }
    }
}
