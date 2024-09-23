import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dao.DepartamentoDAO
import dao.HospitalDAO
import dao.UnidadDAO
import kotlinx.coroutines.launch

@Composable
fun DesktopReportGenerator() {
    val reportParams = mapOf(
        "Resumen del proceso por hospital" to listOf("Hospital"),
        "Resumen de consultas exitosas por hospital" to listOf("Hospital"),
        "Hospitales con mas pacientes" to emptyList<String>(),
        "Resumen de todos los hospitales" to emptyList<String>(),
        "Listado de medicos" to listOf("Hospital", "Departamento (opcional)", "Unidad (opcional)"),
        "Listado de pacientes" to listOf("Hospital", "Departamento (opcional)", "Unidad (opcional)"),
        "Resumen del proceso por departamento" to listOf("Departamento", "Hospital"),
        "Resumen del proceso por unidad" to listOf("Unidad", "Departamento", "Hospital"),
        "Unidades que deben revisar turnos" to listOf("Hospital", "Departamento"),
        "Resumen de consultas exitosas por unidad" to listOf("Unidad", "Departamento", "Hospital")
    )

    var selectedReport by remember { mutableStateOf<String?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var params by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var isParamsFilled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun handleReportSelect(value: String) {
        selectedReport = value
        isDialogOpen = true
        params = emptyMap()
        isParamsFilled = false
    }

    fun handleParamChange(param: String, value: String) {
        params = params.toMutableMap().apply { this[param] = value }
        isParamsFilled = params.values.all { it.isNotEmpty() }
    }

    fun handleDialogClose() {
        isDialogOpen = false
        if (!isParamsFilled) {
            selectedReport = null
        }
    }

    fun handleGeneratePDF() {
        coroutineScope.launch {
            val result = when (selectedReport) {
                "Resumen del proceso por hospital" -> HospitalDAO.resumenProceso(params["Hospital"] ?: "")
                "Resumen de consultas exitosas por hospital" -> HospitalDAO.resumenConsultasExitosas(
                    params["Hospital"] ?: ""
                )

                "Hospitales con mas pacientes" -> HospitalDAO.hospConMasPacient()
                "Resumen de todos los hospitales" -> HospitalDAO.resumen()
                "Listado de medicos" -> {
                    when {
                        params["Unidad (opcional)"]?.isNotEmpty() == true ->
                            HospitalDAO.listadoMedicos(
                                params["Unidad (opcional)"] ?: "",
                                params["Departamento (opcional)"] ?: "",
                                params["Hospital"] ?: ""
                            )

                        params["Departamento (opcional)"]?.isNotEmpty() == true ->
                            HospitalDAO.listadoMedicos(
                                params["Departamento (opcional)"] ?: "",
                                params["Hospital"] ?: ""
                            )

                        else -> HospitalDAO.listadoMedicos(params["Hospital"] ?: "")
                    }
                }

                "Listado de pacientes" -> {
                    when {
                        params["Unidad (opcional)"]?.isNotEmpty() == true ->
                            HospitalDAO.listadoPacientes(
                                params["Hospital"] ?: "",
                                params["Departamento (opcional)"] ?: "",
                                params["Unidad (opcional)"] ?: ""
                            )

                        params["Departamento (opcional)"]?.isNotEmpty() == true ->
                            HospitalDAO.listadoPacientes(
                                params["Hospital"] ?: "",
                                params["Departamento (opcional)"] ?: ""
                            )

                        else -> HospitalDAO.listadoPacientes(params["Hospital"] ?: "")
                    }
                }

                "Resumen del proceso por departamento" -> DepartamentoDAO.resumenProceso(
                    params["Departamento"] ?: "",
                    params["Hospital"] ?: ""
                )

                "Resumen del proceso por unidad" -> UnidadDAO.resumenProceso(
                    params["Unidad"] ?: "",
                    params["Departamento"] ?: "",
                    params["Hospital"] ?: ""
                )

                "Unidades que deben revisar turnos" -> UnidadDAO.revisarTurnos(
                    params["Hospital"] ?: "",
                    params["Departamento"] ?: ""
                )

                "Resumen de consultas exitosas por unidad" -> UnidadDAO.resumenConsultasExitosas(
                    params["Unidad"] ?: "",
                    params["Departamento"] ?: "",
                    params["Hospital"] ?: ""
                )

                else -> null
            }

            if (result != null) {
                val creadorPDF = CreadorPDF()
                creadorPDF.generarPdfDesdeDataClass(
                    outputPath = "./",
                    fileName = "${selectedReport}.pdf",
                    title = selectedReport ?: "Reporte",
                    lista = result
                )
                println("PDF generado: ${selectedReport}.pdf")
            } else {
                println("No se pudo generar el PDF. Resultado nulo.")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(.5f).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Generador de Reportes", style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuDemo(selectedReport, onReportSelect = { handleReportSelect(it) })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { handleGeneratePDF() },
            enabled = isParamsFilled,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(Icons.Filled.FileCopy, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generar PDF")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Filled.FileDownload, contentDescription = null)
        }

        if (isDialogOpen && selectedReport != null) {
            ReportParamsDialog(
                reportParams[selectedReport]!!,
                params,
                onParamChange = { param, value -> handleParamChange(param, value) },
                onClose = { handleDialogClose() },
                isParamsFilled = isParamsFilled
            )
        }
    }
}

@Composable
fun DropdownMenuDemo(selectedReport: String?, onReportSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val reportList = listOf(
        "Resumen del proceso por hospital",
        "Resumen de consultas exitosas por hospital",
        "Hospitales con mas pacientes",
        "Resumen de todos los hospitales",
        "Listado de medicos",
        "Listado de pacientes",
        "Resumen del proceso por departamento",
        "Resumen del proceso por unidad",
        "Unidades que deben revisar turnos",
        "Resumen de consultas exitosas por unidad"
    )

    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedReport ?: "Escoge un reporte")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            reportList.forEach { report ->
                DropdownMenuItem(onClick = {
                    onReportSelect(report)
                    expanded = false
                }) {
                    Text(report)
                }
            }
        }
    }
}

@Composable
fun ReportParamsDialog(
    paramsList: List<String>,
    currentParams: Map<String, String>,
    onParamChange: (String, String) -> Unit,
    onClose: () -> Unit,
    isParamsFilled: Boolean
) {
    Dialog(onCloseRequest = onClose) {
        Surface(
            modifier = Modifier.size(400.dp, 300.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Parámetros del Reporte", style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(8.dp))

                paramsList.forEach { param ->
                    OutlinedTextField(
                        value = currentParams[param] ?: "",
                        onValueChange = { onParamChange(param, it) },
                        label = { Text(param) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = onClose,
                    enabled = isParamsFilled,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Continuar")
                }
            }
        }
    }
}

@Composable
@Preview
fun AppPreview() {
    DesktopReportGenerator()
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            DesktopReportGenerator()
        }
    }
}