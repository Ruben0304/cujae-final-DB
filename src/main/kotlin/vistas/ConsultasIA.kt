package vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.zIndex
import ia.Schema
import ia.generateLlmPrompt
import kotlinx.coroutines.launch
import vistas.componentes.SearchBar
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

@Composable
fun DynamicTable(resultSet: ResultSet) {
    val columnNames = remember { mutableStateListOf<String>() }
    val rows = remember { mutableStateListOf<List<String>>() }
    val surfaceColor = Color(0xFF2D2D2D)
    val headerColor = Color(0xFF3700B3)
    val textColor = Color.White
    val dividerColor = Color(0xFF3D3D3D)

    LaunchedEffect(resultSet) {
        // Limpiar listas para evitar duplicados
        columnNames.clear()
        rows.clear()

        val metaData = resultSet.metaData
        val columnCount = metaData.columnCount

        // Obtener nombres de columnas
        for (i in 1..columnCount) {
            columnNames.add(metaData.getColumnName(i))
        }

        // Obtener datos de filas
        while (resultSet.next()) {
            val row = mutableListOf<String>()
            for (i in 1..columnCount) {
                row.add(resultSet.getString(i) ?: "")
            }
            rows.add(row)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Surface(
            color = surfaceColor,
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Encabezados de columna
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(headerColor)
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    columnNames.forEach { columnName ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = columnName,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                            )
                        }
                    }
                }

                // Filas de datos
                LazyColumn {
                    items(rows.size) { rowIndex ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                rows[rowIndex].forEach { cellData ->
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = cellData,
                                            color = textColor,
                                        )
                                    }
                                }
                            }
                            if (rowIndex < rows.size - 1) {
                                Divider(color = dividerColor, thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConsultaIA() {
    var naturalLanguageQuery by remember { mutableStateOf(TextFieldValue("")) }
    var sqlQuery by remember { mutableStateOf("") }
    var resultSet by remember { mutableStateOf<ResultSet?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Indicador de carga o tabla dinámica se mostrarán encima del campo de entrada y botón
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(16, 78, 146)
                    )
                }
            } else {
                resultSet?.let { rs ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.9f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DynamicTable(rs)
                    }
                }
            }

            // Componente de campo de texto y botón
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.OutlinedTextField(
                        value = naturalLanguageQuery,
                        onValueChange = { naturalLanguageQuery = it },
                        placeholder = { Text("Introduce tu consulta", color = Color.White) },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFF27272A), RoundedCornerShape(50))
                            .padding(end = 0.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            disabledTextColor = Color.White,
                            errorTextColor = Color.Red,
                            focusedContainerColor = Color(0xFF27272A),
                            unfocusedContainerColor = Color(0xFF27272A),
                            disabledContainerColor = Color.DarkGray,
                            errorContainerColor = Color(0xFF27272A),
                            cursorColor = Color.White,
                            focusedPlaceholderColor = Color.White,
                            unfocusedPlaceholderColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    IconButton(
                        onClick = {
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    val llmPrompt = generateLlmPrompt(Schema.schema, naturalLanguageQuery.text)
                                    sqlQuery = llmPrompt


                                    // Ejecutar la consulta SQL
                                    val connection: Connection = DriverManager.getConnection(
                                        "jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:6543/postgres",
                                        "postgres.wgfdmgsbsqflcgtecglw",
                                        "@flowFerxxo2024"
                                    )
                                    val statement = connection.prepareStatement(sqlQuery)
                                    resultSet = statement.executeQuery()


                                    isLoading = false

                                } catch (e: Exception) {
                                    isLoading = false
                                    e.printStackTrace()
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(Color(0xFF22C55E), CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Ejecutar consulta",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}