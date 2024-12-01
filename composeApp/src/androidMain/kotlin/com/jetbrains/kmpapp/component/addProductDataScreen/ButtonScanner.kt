package com.jetbrains.kmpapp.component.addProductDataScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.jetbrains.kmpapp.screens.addProduct.AddProductViewModel
import com.jetbrains.kmpapp.screens.addProduct.ProductType
import com.jetbrains.kmpapp.utils.TypeCamera

@Composable
fun ButtonScanner(viewModel: AddProductViewModel, productType: ProductType, navController : NavHostController) {
    val context = LocalContext.current
    var resultScanner by rememberSaveable { mutableStateOf("") }
    val cameraScreenLaunch = viewModel.takePhoto.collectAsState()
    val scanLauncher = rememberLauncherForActivityResult(contract = ScanContract(), onResult = { result ->
        resultScanner = result.contents ?: ""
        viewModel.resultScanner(resultScanner)
        viewModel.loadRudacData(resultScanner, context)
    })

        if(cameraScreenLaunch.value){
            navController.navigate("camera_screen/${TypeCamera.DOCUMENT1}"){
                launchSingleTop = true
                navController.navigateUp()
                viewModel.accessCamera(false)
            }
        }

    val handleButtonClick = {
        when (productType) {
            ProductType.REPLACEMENT -> {
                scanLauncher.launch(
                    ScanOptions()
                        .setBeepEnabled(true)
                        .setPrompt("Escanea el Código RUDAC")
                        .setOrientationLocked(true)
                )
            }
            ProductType.VEHICLE -> {
                viewModel.accessCamera(true)
            }
        }
    }

    CustomButton(
        text = when (productType) {
            ProductType.REPLACEMENT -> "Escaneá el código RUDAC"
            ProductType.VEHICLE -> "Cargar certificado de desarme"
        },
        onClick = handleButtonClick
    )
}

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D43))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Localized description")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, textAlign = TextAlign.Center)
        }
    }
}