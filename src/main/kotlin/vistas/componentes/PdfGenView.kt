package vistas.componentes

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun DesktopReportGenerator() {
    val reportParams = mapOf(
        "ventas" to listOf("Fecha inicio", "Fecha fin", "Categoría"),
        "inventario" to listOf("Almacén", "Categoría"),
        "clientes" to listOf("Tipo de cliente", "Región"),
        "financiero" to listOf("Año", "Trimestre", "Departamento")
    )

    var selectedReport by remember { mutableStateOf<String?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var params by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var isParamsFilled by remember { mutableStateOf(false) }

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
        println("Generando PDF para el reporte: $selectedReport con parámetros: $params")
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
    val reportList = listOf("ventas", "inventario", "clientes", "financiero")

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
                    Text(report.capitalize())
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
