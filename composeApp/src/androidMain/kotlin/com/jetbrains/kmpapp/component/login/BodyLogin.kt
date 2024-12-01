package com.jetbrains.kmpapp.component.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Switch
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.colors.grayChip
import com.jetbrains.kmpapp.colors.greenIcon

@Composable
fun BodyLogin(
    email: String,
    loginError: Boolean,
    errorMessageEmail: String,
    password: String,
    isPasswordVisible: Boolean,
    errorMessagePassword: String,
    offlineSession: Boolean,
    login:()-> Unit,
    accessOfflineMode: Boolean,
    emailChange: (String) -> Unit,
    passwordChange: (String) -> Unit,
    changeAccessMode: (Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#F3FCF7")))
    ) {

        item {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                RenderLogo()
            }
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                EmailField(
                    value = email,
                    textChange = emailChange,
                    isError = loginError,
                    errorMessage = errorMessageEmail
                )
                PasswordField(
                    value = password,
                    onValueChange = passwordChange,
                    isPasswordVisible = isPasswordVisible,
                    isError = loginError,
                    errorMessage = errorMessagePassword
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))

                if (offlineSession) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextOfflineSession(Modifier.weight(4f))
                        SwitchSession(
                            status = accessOfflineMode,
                            onChange = changeAccessMode,
                            modifier = Modifier
                        )
                    }
                    Spacer(modifier = Modifier.padding(32.dp))
                }

            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 16.dp, end = 16.dp ),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonLogin(action = { login()})
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 50.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(fontSize = 16.sp, text = "¿Olvidaste la contraseña?")
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    text = "Contactate a soporte@reciklo.com.ar para recuperarla"
                )
            }
        }
    }
}

@Composable
private fun RenderLogo() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        AsyncImage(
            model = R.drawable.reciklo_logo, contentDescription = "logo app",
            modifier = Modifier
                .height(230.dp)
                .width(230.dp)
        )
    }
}

@Composable
private fun TextOfflineSession(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(end = 12.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Estado de conexion",
            modifier = Modifier.padding(bottom = 5.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Ingresando sin conexión tus datos serán guardados localmente. Podrás  sincronizarlos cuando ingreses con conexión.",
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ButtonLogin(action: () -> Unit) {
    ElevatedButton(
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 10.dp),
        onClick = { action() },
        colors = ButtonDefaults.elevatedButtonColors(
            Color(
                android.graphics.Color.parseColor("#296A48")
            ),
            Color(
                android.graphics.Color.parseColor("#FFFFFF")
            ),
            Color(
                android.graphics.Color.parseColor("#56A478")
            ),
            Color(
                android.graphics.Color.parseColor("#F3FCF7")
            )
        ),
        modifier = Modifier.fillMaxWidth().height(50.dp)
    ) {
        Text(text = "Acceder", fontSize = 16.sp)
    }
}

@Composable
private fun SwitchSession(status: Boolean, onChange: (Boolean) -> Unit, modifier: Modifier) {
    Switch(modifier = modifier,
        checked = status,
        colors = SwitchDefaults.colors(
            checkedTrackColor = greenIcon,
            uncheckedTrackColor = grayChip
            ),
        onCheckedChange = {
            onChange(it)
        },
    )
}