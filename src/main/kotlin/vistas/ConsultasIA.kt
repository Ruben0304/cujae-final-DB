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
import vistas.componentes.DynamicTable
import vistas.componentes.SearchBar
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet



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