package vistas.login

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.Auth
import global.Global
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vistas.DashboardApp
import vistas.colores.textColor
import vistas.componentes.ShowToast
import vistas.componentes.ToastHost
import vistas.componentes.ToastManager
import vistas.componentes.ToastType

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Transparent),
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
                    modifier = Modifier
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource("profile.jpg"),
                        contentDescription = "User Icon",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )

                    Text(
                        text = "Iniciar sesión",
                        color = Color.DarkGray,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 24.dp, top = 15.dp)
                    )


                    CustomTextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = "Usuario",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Contraseña",
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    var buttonState by remember { mutableStateOf(ButtonState.Idle) }

                    Button(
                        onClick = {
                            buttonState = ButtonState.Loading
                            coroutineScope.launch {
                                val session = Auth.login(username.text, password.text)
                                if (session != null) {
                                    buttonState = ButtonState.Finished
                                } else {
                                    ToastManager.showToast("Credenciales incorrectas", ToastType.ERROR)
                                    buttonState = ButtonState.Idle
                                    // Implement your error toast here
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
                        enabled = buttonState != ButtonState.Loading
                    ) {
                        AnimatedContent(
                            targetState = buttonState,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(150, delayMillis = 150)) with
                                        fadeOut(animationSpec = tween(150))
                            }
                        ) { state ->
                            when (state) {
                                ButtonState.Idle -> Text("Entrar", fontSize = 18.sp)
                                ButtonState.Loading -> CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )

                                ButtonState.Finished -> {
                                    Text("¡Bienvenido!", fontSize = 18.sp)
                                    coroutineScope.launch {
                                        delay(1000)
                                        buttonState = ButtonState.Idle
                                        onLoginSuccess()
                                    }
                                }

                            }
                        }
                    }


                }
            }
        }
    }
    ToastHost()
}
