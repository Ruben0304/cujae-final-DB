package vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.Auth
import dao.CantidadesDAO.obtenerConteoPacientesPorEstado
import dao.ConsultaDAO
import dao.DoctorDAO
import dao.PatientDAO
import global.Global
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import modelos.Consulta
import modelos.PacienteC
import modelos.RegistroC
import supabase.Supabase
import vistas.colores.SoftBlue
import vistas.colores.SoftGreen
import vistas.colores.SoftRed
import vistas.colores.textColor
import vistas.componentes.ToastManager
import vistas.componentes.ToastType
import vistas.componentes.WelcomeAvatar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import vistas.form.create.CreateConsultaForm


@Composable
fun DashboardContentMedico() {
    var showDialog by remember { mutableStateOf(false) }

    var consultasHoy by remember { mutableStateOf(0) }
    var proximaConsulta by remember { mutableStateOf<Consulta?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {


        try {
            val consultas = ConsultaDAO.getConsultasMedico()
            consultasHoy = ConsultaDAO.filtrarConsultasHoy(consultas)
            proximaConsulta = ConsultaDAO.obtenerProximaConsulta(consultas)

        } catch (e: Exception) {
            ToastManager.showToast(e.message.toString(), ToastType.INFO)
        }


        isLoading = false
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(435.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box {

                        Column {
                            Spacer(Modifier.height(30.dp))
                            CreateConsultaForm()
                        }
                        // Botón de cerrar en la esquina superior derecha
                        IconButton(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.Red
                            )
                        }

                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Transparent)
    ) {
        // Agregar el componente de bienvenida en la parte superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Espacio vacío a la izquierda para mantener el avatar a la derecha
            Spacer(modifier = Modifier.weight(1f))

            // Componente de bienvenida
            WelcomeAvatar()
        }
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(color = Color.Blue)
            }
        } else {

            // Agregar dropdown filtrador y estilo futurista
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                // Componente de dropdown aquí
            }

            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                StatCard(
                    "Hoy",
                    "$consultasHoy consultas",
                    Icons.Outlined.CalendarToday,
                    SoftBlue
                )
                Spacer(modifier = Modifier.width(16.dp))
                proximaConsulta?.let {
                    StatCard(
                        "Próxima consulta",
                        if (proximaConsulta != null)
                            formatFechaHora(it.getFormattedFechaHora())
                        else
                            "No tienes",
                        Icons.Outlined.Schedule,
                        SoftRed
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                StatCard(
                    "Siguiente paciente",
                    if (proximaConsulta != null)
                    "${proximaConsulta?.registro?.paciente?.nombre} ${proximaConsulta?.registro?.paciente?.apellidos}"
                    else
                        "",
                    Icons.Outlined.Sick,
                    SoftGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(265.dp)
                    .padding(10.dp),

                color = Color.White


            ) {
                Column(
                    modifier = Modifier.padding(10.dp).fillMaxSize(),

                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
// Resumen del día

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Hola ${Auth.nombre} ${Auth.apellidos} 👋🏼",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 10.dp),
                            color = textColor
                        )
                        Text(
                            "Tienes un día ocupado por delante. Recuerda tomar descansos entre consultas y mantenerte hidratado.",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }


                    Spacer(modifier = Modifier.height(50.dp))

                    // Botón para agendar nueva consulta
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        elevation = ButtonDefaults.elevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff151515))
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agendar",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White

                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Agendar nueva consulta", color = Color.White)
                    }
                }
            }
        }
    }
}


@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color) {
    Surface(
//        backgroundColor = Color.Transparent,
        elevation = 12.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
            )


    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, color = Color(110, 109, 109))
                Text(value, color = textColor, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }
    }
}

fun formatFechaHora(fechaHoraString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a")
    val fechaHora = LocalDateTime.parse(fechaHoraString, formatter)
    val hoy = LocalDate.now()

    return when {
        fechaHora.toLocalDate() == hoy -> {
            // Si es hoy, solo muestra la hora
            fechaHora.format(DateTimeFormatter.ofPattern("hh:mm a"))
        }

        fechaHora.toLocalDate().year == hoy.year -> {
            // Si es este año, muestra mes/día hora
            fechaHora.format(DateTimeFormatter.ofPattern("MM/dd hh:mm a"))
        }

        else -> {
            // Si es otro año, muestra año/mes/día hora
            fechaHora.format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a"))
        }
    }
}