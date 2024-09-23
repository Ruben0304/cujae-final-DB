package vistas.form.create


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import auth.Auth
import dao.DepartamentoDAO
import dao.HospitalDAO
import dao.UnidadDAO
import kotlinx.coroutines.launch
import modelos.Departamento
import modelos.HospitalNombres
import vistas.componentes.SubmitButton
import vistas.componentes.TextInputField
import vistas.login.CustomDropdown


@Composable
fun CreateUnidadForm() {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf<String?>(null) }
    var departamentos by remember { mutableStateOf(listOf<Departamento>()) }
    val corrutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        departamentos = DepartamentoDAO.obtenerDepartamentosPorHospital(Auth.hospital)
        departamento = departamentos[0].departamento_codigo
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            TextInputField(value = codigo, onValueChange = { codigo = it }, label = "Código de unidad")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = nombre, onValueChange = { nombre = it }, label = "Nombre de unidad")
            Spacer(modifier = Modifier.height(16.dp))
            TextInputField(value = ubicacion, onValueChange = { ubicacion = it }, label = "Ubicación")
            Spacer(modifier = Modifier.height(16.dp))
            if (departamento != null)
            CustomDropdown(
                items = departamentos.map { d -> d.departamento_nombre },
                selectedItem = departamentos.first { d -> d.departamento_codigo == departamento }.departamento_nombre,
                onItemSelected = {
                    departamento =
                        departamentos.first { d -> d.departamento_nombre == it }.departamento_codigo
                },
                placeholder = "Departamento",
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            SubmitButton(
                onClicked = {
                    corrutineScope.launch {
                        if (departamento != null)
                            UnidadDAO.crearUnidad(
                                codigo,
                                nombre,
                                ubicacion,
                                departamento!!,
                                Auth.hospital
                            )
                    }
                }
            )
        }
    }
}
