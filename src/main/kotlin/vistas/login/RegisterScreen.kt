package vistas.login

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import auth.Auth
import dao.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelos.Departamento
import modelos.HospitalNombres
import modelos.Unidad
import vistas.colores.textColor
import vistas.componentes.SelectInputField
import vistas.componentes.ToastManager
import vistas.componentes.ToastType

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen() {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }

    var xp by remember { mutableStateOf(TextFieldValue("")) }
    var licencia by remember { mutableStateOf(TextFieldValue("")) }
    var telefono by remember { mutableStateOf(TextFieldValue("")) }
    var especialidad by remember { mutableStateOf(TextFieldValue("")) }
    var departamento by remember { mutableStateOf<String?>(null) }
    var unidad by remember { mutableStateOf<String?>(null) }

    var newRole by remember { mutableStateOf("") }
    var newHospital by remember { mutableStateOf(Auth.hospital) }
    var hospitales by remember { mutableStateOf(listOf<HospitalNombres>()) }
    var departamentos by remember { mutableStateOf(listOf<Departamento>()) }
    var unidades by remember { mutableStateOf(listOf<Unidad>()) }
    var isVisible by remember { mutableStateOf(false) }
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isVisible = true
        hospitales = HospitalDAO.getHospitals()
        departamentos = DepartamentoDAO.obtenerDepartamentosPorHospital(Auth.hospital)
        departamento = departamentos[0].departamento_codigo
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(1000)) + expandVertically(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000)) + shrinkVertically(animationSpec = tween(1000))
        ) {
            androidx.compose.material.Surface(
                elevation = 10.dp,
                modifier = Modifier
                    .width(400.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagen de perfil y título: Estáticos
                    Image(
                        painter = painterResource("profile.jpg"),
                        contentDescription = "User Icon",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Crear cuenta",
                        color = Color.DarkGray,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 24.dp, top = 15.dp)
                    )

                    // Zona scrolleable: Inputs y Dropdowns
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(bottom = 16.dp) // Espacio antes del botón
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                        ) {
                            CustomTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                placeholder = "Nombre",
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            CustomTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                placeholder = "Apellido",
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            CustomTextField(
                                value = username,
                                onValueChange = { username = it },
                                placeholder = "Email",
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            CustomTextField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = "Contraseña",
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.padding(bottom = 24.dp)
                            )

                            when (Auth.rol) {
                                "admin_hospital" -> {
                                    CustomDropdown(
                                        items = listOf("medico"),
                                        selectedItem = newRole,
                                        onItemSelected = { newRole = it },
                                        placeholder = "Rol",
                                        modifier = Modifier.padding(bottom = 24.dp)
                                    )
                                    if (departamento != null)
                                        CustomDropdown(
                                            items = departamentos.map { d -> d.departamento_nombre },
                                            selectedItem = departamentos.first { d -> d.departamento_codigo == departamento }.departamento_nombre,
                                            onItemSelected = {
                                                departamento =
                                                    departamentos.first { d -> d.departamento_nombre == it }.departamento_codigo
                                                coroutineScope.launch {
                                                    unidades = UnidadDAO.obtenerUnidadesPorHospitalYDepartamento(
                                                        Auth.hospital,
                                                        departamento!!
                                                    )
                                                    if (unidades.isNotEmpty())
                                                        unidad = unidades[0].codigo
                                                }
                                            },
                                            placeholder = "Departamento",
                                            modifier = Modifier.padding(bottom = 24.dp)
                                        )

                                    if (unidad != null)
                                        CustomDropdown(
                                            items = unidades.map { u -> u.nombre },
                                            selectedItem = unidades.first { u -> u.codigo == unidad }.nombre.orEmpty(),
                                            onItemSelected = {
                                                unidad = unidades.first { u -> u.nombre == it }.codigo
                                            },
                                            placeholder = "Unidad",
                                            modifier = Modifier.padding(bottom = 24.dp)
                                        )

                                    CustomTextField(
                                        value = especialidad,
                                        onValueChange = { especialidad = it },
                                        placeholder = "Especialidad",
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    CustomTextField(
                                        value = telefono,
                                        onValueChange = { telefono = it },
                                        placeholder = "Telefono",
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    CustomTextField(
                                        value = xp,
                                        onValueChange = { xp = it },
                                        placeholder = "Años de experiencia",
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    CustomTextField(
                                        value = licencia,
                                        onValueChange = { licencia = it },
                                        placeholder = "No Licencia",
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                }

                                "admin_general" -> {
                                    CustomDropdown(
                                        items = listOf("admin_hospital", "admin_general"),
                                        selectedItem = newRole,
                                        onItemSelected = { newRole = it },
                                        placeholder = "Rol",
                                        modifier = Modifier.padding(bottom = 24.dp)

                                    )

                                    if (newRole == "admin_hospital") {
                                        CustomDropdown(
                                            items = hospitales.map { h -> h.nombre },
                                            selectedItem = hospitales.first { h -> h.codigo == newHospital }.nombre,
                                            onItemSelected = {
                                                newHospital = hospitales.first { h -> h.nombre == it }.codigo
                                            },
                                            placeholder = "Hospital",
                                            modifier = Modifier.padding(bottom = 24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }


// Botón estático
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                buttonState = ButtonState.Loading
                                try {
                                    if (newRole == "medico") {
                                        val medicoCreado = DoctorDAO.crearMedico(
                                            nombre = firstName.text,
                                            apellidos = lastName.text,
                                            numeroLicencia = licencia.text,
                                            aniosExperiencia = xp.text.toIntOrNull() ?: 0,
                                            datosContacto = username.text,
                                            especialidad = especialidad.text,
                                            telefono = telefono.text,
                                            departamentoCodigo = departamento,
                                            unidadCodigo = unidad,
                                            hospitalCodigo = Auth.hospital
                                        )

                                        if (medicoCreado != null) {
                                          val usuarioCreado = Auth.crear_usuario(
                                                username = username.text,
                                                passwordP = password.text,
                                                nombre = firstName.text,
                                                apellido = lastName.text,
                                                rol = newRole,
                                                hospital = Auth.hospital
                                            )
                                            if (usuarioCreado != null)
                                               AccountDAO.vincularMedicoCuenta(medico = medicoCreado,usuarioCreado.id)

                                        } else {
                                            throw Exception("No se pudo crear el médico")
                                        }

                                    } else {
                                        Auth.crear_usuario(
                                            username = username.text,
                                            passwordP = password.text,
                                            nombre = firstName.text,
                                            apellido = lastName.text,
                                            rol = newRole,
                                            hospital = if (newHospital.isNotEmpty()) newHospital else null
                                        )
                                    }
                                    buttonState = ButtonState.Finished
                                    ToastManager.showToast("Cuenta creada con éxito", ToastType.SUCCESS)
                                } catch (e: Exception) {
                                    buttonState = ButtonState.Idle
                                    ToastManager.showToast("Error al crear cuenta: ${e.message}", ToastType.ERROR)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .graphicsLayer {
                                shape = RoundedCornerShape(25.dp)
                                clip = true
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xef070000),
                            contentColor = Color.White
                        ),
                        enabled = buttonState == ButtonState.Idle
                    ) {
                        when (buttonState) {
                            ButtonState.Idle -> Text("Crear cuenta", fontSize = 18.sp)
                            ButtonState.Loading -> CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )

                            ButtonState.Finished -> Text("¡Cuenta creada!", fontSize = 18.sp)
                        }
                    }


                }
            }
        }
    }
}


@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true
            ),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxSize()
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = 16.sp
                ),
                visualTransformation = visualTransformation,
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (value.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun CustomDropdown(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth() // Esto solo afecta al botón desplegable, no al menú
            .height(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true
            )
            .clickable(enabled = enabled) { expanded = true }
            .alpha(if (enabled) 1f else 0.6f),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedItem.ifEmpty { placeholder },
                    color = if (selectedItem.isEmpty()) Color.Gray else Color.Black,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = if (enabled) Color.Gray else Color.LightGray
                )
            }
            DropdownMenu(
                expanded = expanded && enabled,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(IntrinsicSize.Min) // Ajusta el ancho al tamaño mínimo necesario
                    .background(Color.White)
            ) {
                items.forEach { item ->
                    androidx.compose.material.DropdownMenuItem(
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .height(40.dp) // Ajustar la altura de cada ítem
                    ) {
                        Text(
                            text = item,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


enum class ButtonState {
    Idle, Loading, Finished
}