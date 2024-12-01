package com.jetbrains.kmpapp.component.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import com.jetbrains.kmpapp.colors.greenIcon
import com.jetbrains.kmpapp.colors.greenOnline
import com.jetbrains.kmpapp.colors.textColorDefault
import com.jetbrains.kmpapp.colors.redIcon
import com.jetbrains.kmpapp.colors.redOffline
import com.jetbrains.kmpapp.component.ShimmerAnimationOptimized
import com.jetbrains.kmpapp.component.StatusSession
import com.jetbrains.kmpapp.component.visible
import com.jetbrains.kmpapp.domain.models.Profile
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.navigation.AppScreens
import org.koin.compose.koinInject

@Composable
fun Dashboard(
    navController: NavHostController,
    profile: StatusResult<Profile>?
) {
    val sessionManager : SessionManagerUseCase = koinInject()
    val session = sessionManager.session.collectAsState()
    var name by remember{ mutableStateOf("") }
    var status by remember { mutableStateOf(false) }

    when(profile){
        is StatusResult.Error ->  {
            status = true
        }
        is StatusResult.Success -> {
            name = profile.value.firstName
            status = true
        }
        null -> {}
    }

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(40.dp).padding(top = 8.dp)){
            when(session.value){
                is SessionManager.Offline -> {
                    TextProfile( modifier = Modifier.weight(1f),"¡Hola!", status)
                    StatusSession("Inactivo", redOffline, redIcon, Icons.Filled.WifiOff)
                    status = true
                }
                is SessionManager.Online -> {
                    TextProfile( modifier = Modifier.weight(1f),"¡Hola,", status)
                    StatusSession("Activo", greenOnline, greenIcon, Icons.Filled.Wifi)
                }
            }
        }
        TextProfile( modifier = Modifier,"$name!", status)

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Comenzá la carga de stock de productos",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        ElevatedButton(
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 10.dp),
            onClick = {
                navController.navigate(AppScreens.OptionTakePhoto.route) {
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = greenIcon)
        ) {
            Text(text = "+ Cargar", fontSize = 16.sp)
        }
    }
}

@Composable
fun TextProfile(modifier: Modifier, name:String, status:Boolean) {
    Row(modifier = modifier.fillMaxWidth()){
        if (name.isNotEmpty() && name !="!")
        Text(
            modifier = Modifier.visible(status),
            text = name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = textColorDefault,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.W400,
            fontSize = 28.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        ShimmerAnimationOptimized(modifier = Modifier.fillMaxWidth().height(25.dp).visible(!status).clip(RoundedCornerShape(15.dp)))
    }
}