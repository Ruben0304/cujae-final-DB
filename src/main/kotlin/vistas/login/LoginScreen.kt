package vistas.login
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.Auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vistas.componentes.ShowToast
import vistas.componentes.ToastHost
import vistas.componentes.ToastManager
import vistas.componentes.ToastType

@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    var showDashboardApp by remember { mutableStateOf(false) }
    var isButtonLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var errorAuthToast by remember { mutableStateOf(false) }




    if (showDashboardApp) {
        DefaultPreview()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            // Fondo con imagen oscurecida
            Image(
                painter = painterResource("fondo-login.png"), // Asegúrate de tener esta imagen en tus recursos
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )

            // Contenido del login
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .width(500.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Iniciar sesión",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Usuario", color = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1E1E1E),
                                unfocusedContainerColor = Color(0xFF1E1E1E),
                                cursorColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña", color = Color.Gray) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1E1E1E),
                                unfocusedContainerColor = Color(0xFF1E1E1E),
                                cursorColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        Button(
                            onClick = {
                                scope.launch {
                                    isButtonLoading = true
                                    val session = Auth.login(username.text, password.text)
                                    isButtonLoading = false

                                    if (session != null) {
                                        // Autenticación exitosa
                                        isLoading = true
                                        showDashboardApp = true
                                    } else {
                                        ToastManager.showToast("Credenciales incorrectas", ToastType.ERROR)
                                        // Implement your error toast here
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ),
                            enabled = !isButtonLoading
                        ) {
                            if (isButtonLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "Iniciar sesión",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }
                }
            }

//            if (isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .size(48.dp)
//                        .align(Alignment.Center),
//                    color = Color.Red
//                )
//            }
            ToastHost()
        }
    }
}