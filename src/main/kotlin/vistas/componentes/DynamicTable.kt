package vistas.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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