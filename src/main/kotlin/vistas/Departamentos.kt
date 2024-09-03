package vistas

import PatientListContent
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import auth.Auth
import dao.DepartamentoDAO
import global.Global
import kotlinx.coroutines.launch
import modelos.Departamento



@Composable
fun DepartamentoTable(navController: NavHostController) {
    val surfaceColor = Color(0xFF2D2D2D)
    val headerColor = Color(0xFF3700B3)
    val textColor = Color.White
    val dividerColor = Color(0xFF3D3D3D)
    val linkColor = Color(0xFF64B5F6)
    var departamentos by remember { mutableStateOf(listOf<Departamento>()) }
    var isLoading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            if (Global.selectedHospital != null)
                departamentos = DepartamentoDAO.obtenerDepartamentosPorHospital(Global.selectedHospital)
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(16, 78, 146))
            }
        } else {
            Surface(
                color = surfaceColor,
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerColor)
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        listOf("Cod", "Nombre", "").forEach { header ->
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = header,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                )
                            }
                        }
                    }

                    // Rows
                    LazyColumn {
                        itemsIndexed(departamentos) { index, departamento ->
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) { Text(departamento.departamento_codigo, color = textColor) }
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) { Text(departamento.departamento_nombre, color = textColor) }

                                    // Ver link
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Ver unidades",
                                            color = linkColor,
                                            modifier = Modifier
                                                .clickable {
                                                    navController.navigate("unidades/${departamento.departamento_codigo}")
                                                }
                                                .padding(4.dp)
                                        )
                                    }

                                }
                                if (index < departamentos.size - 1) {
                                    Divider(color = dividerColor, thickness = 1.dp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
