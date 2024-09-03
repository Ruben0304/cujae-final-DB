package vistas.form


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dao.DepartamentoDAO
import kotlinx.coroutines.launch
import vistas.componentes.SubmitButton
import vistas.componentes.TextInputField

@Composable
fun CreateDepartamentoForm() {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var hospitalCodigo by remember { mutableStateOf("") }
    val corrutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            TextInputField(value = codigo, onValueChange = { codigo = it }, label = "Código de departamento")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = nombre, onValueChange = { nombre = it }, label = "Nombre de departamento")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(
                value = hospitalCodigo,
                onValueChange = { hospitalCodigo = it },
                label = "Código de hospital"
            )
            Spacer(modifier = Modifier.height(24.dp))
            SubmitButton(
                onClicked = {
                    corrutineScope.launch {
                        DepartamentoDAO.crearDepartamento(
                            codigo,
                            nombre,
                            hospitalCodigo
                        )
                    }
                }
            )
        }
    }
}