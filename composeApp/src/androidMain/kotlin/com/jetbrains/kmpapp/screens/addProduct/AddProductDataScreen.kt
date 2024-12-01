package com.jetbrains.kmpapp.screens.addProduct

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.component.addProductDataScreen.demerit.ItemFailureProduct
import com.jetbrains.kmpapp.component.addProductDataScreen.galleryImageList.ListGalleryPhotos
import com.jetbrains.kmpapp.component.addProductDataScreen.ButtonScanner
import com.jetbrains.kmpapp.component.addProductDataScreen.InputProductData
import com.jetbrains.kmpapp.component.addProductDataScreen.Demeritos
import com.jetbrains.kmpapp.component.ButtonNextStep
import com.jetbrains.kmpapp.component.Loading
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.User
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import org.koin.androidx.compose.get
import com.jetbrains.kmpapp.screens.addProduct.components.ImagePreviewDialog
import com.jetbrains.kmpapp.utils.CameraUtils
import com.jetbrains.kmpapp.utils.TypeCamera
import com.jetbrains.kmpapp.utils.allPermissionsGranted
import org.koin.compose.koinInject

@Composable
fun AddProductDataScreen(navController: NavHostController, viewModel: AddProductViewModel) {
    val listFailure = viewModel.listProductFailureUri.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val listUris =  viewModel.urisPhotos.collectAsState()
    val sessionUseCase = koinInject<SessionManagerUseCase>()
    val session = sessionUseCase.session
    val productType = viewModel.saveProductTyoe.collectAsState().value

    val previewImage = viewModel.showDialogImagePreview.collectAsState()
    val vehicleCompleteLogin = viewModel.vehicleCompleteToken.collectAsState()
    viewModel.fetchManufacturerCar(session.value)
    viewModel.fetchCategory()

    var allPermissionsGranted by remember{ mutableStateOf(allPermissionsGranted(context)) }

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){granted ->
        if (granted.all { it.value }){
            allPermissionsGranted = true
        }
    }

    if(previewImage.value != null){
        ImagePreviewDialog(
            imageUri =previewImage.value,
            onAccept = {viewModel.accessCamera(false)},
            onDecline = null,
            onDismiss = {
                viewModel.setShowDialogImagePreview(null)
            }
        )
    }

    LaunchedEffect(session) {
        when (session.value) {
            is SessionManager.Offline -> {
                viewModel.fetchTypeProduct("")
            }

            is SessionManager.Online -> {
                viewModel.fetchTypeProduct((session.value as SessionManager.Online<User>).value.token)
            }
        }
    }

    LaunchedEffect(viewModel.isSuccessPhoto) {
        viewModel.isSuccessPhoto.collect {
            if (it) {
                viewModel.saveSuccessPhoto(context)
            }
        }
    }

    LazyColumn {
        item {
            ListGalleryPhotos(listUris = listUris.value,
                onSelectImages = viewModel::onSelectImages,
                deletePhotoSelected = viewModel::deletePhotoSelected,
                setShowDialogImagePreview = viewModel::setShowDialogImagePreview)
            Box(modifier = Modifier.padding(10.dp)){
                Column {
                    SwitchButtonProductTypes (
                        { viewModel.setProductType(it) },
                        productType,
                        vehicleCompleteLogin.value,
                        {
                            viewModel.loginVehicleComplete()
                        }
                    )
                    when (productType) {
                        ProductType.REPLACEMENT -> {
                            ButtonScanner(viewModel, productType = ProductType.REPLACEMENT, navController )
                            InputProductData( viewModel, productType = ProductType.REPLACEMENT)
                        }
                        ProductType.VEHICLE -> {
                            ButtonScanner(viewModel, productType = ProductType.VEHICLE, navController )
                            InputProductData( viewModel, productType = ProductType.VEHICLE)
                        }
                    }
                    Demeritos(viewModel){
                        if (allPermissionsGranted) {
                            navController.navigate("camera_screen/${TypeCamera.DEMERIT}") {
                                navController.previousBackStackEntry
                            }
                        } else {
                            permissionsLauncher.launch(CameraUtils.REQUIRED_PERMISSIONS)
                        }
                    }
                    if (listFailure.value.isNotEmpty())
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp, start = 12.dp, end = 12.dp)) {
                            listFailure.value.onEach {
                                ItemFailureProduct(description = it.failure, uri = it.uri.toUri())
                            }
                        }
                    ButtonNextStep(
                        nextStep = {
                            viewModel.saveProduct()
                            viewModel.setButtonPressed(true)
                            if(viewModel.setStateOfData(context,productType)){
                                navController.navigate("detail_product") {
                                    popUpTo("add_product_data_screen") {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        },
                        "Siguiente",
                    )
                }
            }
        }
    }
    if(isLoading.value){
        Loading()
    }

}


enum class ProductType {
    REPLACEMENT,
    VEHICLE
}

@Composable
fun SwitchButtonProductTypes(
    onChangeTypeSelected: (ProductType) -> Unit,
    productType: ProductType,
    vehicleCompleteLogin : String?,
    loginVehicleComplete: () -> Unit
) {

    val context = LocalContext.current
    val sessionManager : SessionManagerUseCase = get()
    val session = sessionManager.session.collectAsState()
    var selectedType by remember {
        when (session.value) {
            is SessionManager.Offline -> {
                mutableStateOf(ProductType.REPLACEMENT)
            }

            is SessionManager.Online -> {
                mutableStateOf(productType)
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Tipo de producto",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SwitchButton(
                text = "Auto Completo",
                icon = R.drawable.icon_vehicle_complete,
                isSelected = selectedType == ProductType.VEHICLE,
                onClick = {
                    when(session.value){
                        is SessionManager.Offline -> {
                            Toast.makeText(context, "Debe iniciar sesión", Toast.LENGTH_SHORT).show()
                        }
                        is SessionManager.Online -> {
                            loginVehicleComplete()
                            if( vehicleCompleteLogin != null) {
                                selectedType = ProductType.VEHICLE
                                onChangeTypeSelected(ProductType.VEHICLE)
                            } else {
                                Toast.makeText(context, "Error de Token Vehiculo Completo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
            )
            SwitchButton(
                text = "Repuesto",
                icon = R.drawable.icon_replacement,
                isSelected = selectedType == ProductType.REPLACEMENT,
                onClick = {
                    selectedType = ProductType.REPLACEMENT
                    onChangeTypeSelected(ProductType.REPLACEMENT)
                },
                modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
fun SwitchButton(
    text: String,
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AssistChip(
        shape = RoundedCornerShape(30.dp),
        onClick = { onClick() },
        border = AssistChipDefaults.assistChipBorder(borderColor = Color.Gray, enabled = true),
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    tint = if (isSelected) Color(0xFF006D43) else Color.Gray,
                    contentDescription = "Estado de app",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = if (isSelected) Color(0xFF006D43) else Color.Gray,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isSelected) Color(0xFFEAF3EC) else Color.White,
            labelColor = if (isSelected) Color(0xFF006D43) else Color.Gray
        ),
        modifier = modifier
            .height(35.dp)
            .fillMaxWidth()
    )
}
