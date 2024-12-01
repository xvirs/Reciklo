package com.jetbrains.kmpapp.screens.addProduct

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.colors.bottomBarSelectedItemColor
import com.jetbrains.kmpapp.colors.greenIcon
import com.jetbrains.kmpapp.component.Loading
import com.jetbrains.kmpapp.component.addProductDataScreen.cameraScreen.CameraPreview
import com.jetbrains.kmpapp.component.addProductDataScreen.cameraScreen.takePhoto
import com.jetbrains.kmpapp.screens.addProduct.components.ImagePreviewDialog
import com.jetbrains.kmpapp.utils.TypeCamera
import com.jetbrains.kmpapp.screens.addProduct.components.CameraScanDataVehicleComplete
import kotlinx.coroutines.delay

@Composable
fun CameraScreenAddProduct(viewModel: AddProductViewModel, typeCamera : String, navigate:() ->Unit, visibilityBar:(Boolean)->Unit) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val isLoading = viewModel.isLoading.collectAsState()
    val isButtonEnabled by viewModel.buttonTakePhoto.collectAsState()
    var showModal by remember { mutableStateOf(true) }
    var showFirstImage by remember { mutableStateOf(true) }

    if(!showModal){
        LaunchedEffect(Unit) {
            delay(5000L)
            showFirstImage = false
        }
    }


    val stringToEnum : TypeCamera? = enumValues<TypeCamera>().find {
        it.name.equals(typeCamera, ignoreCase = true)
    }

    LaunchedEffect(viewModel.isSuccessPhoto){
        viewModel.isSuccessPhoto.collect{
            if (it){
                viewModel.saveSuccessPhoto(context)
            }
        }
    }

    val previewImage = viewModel.showDialogImagePreview.collectAsState()

    if(previewImage.value != null){
        ImagePreviewDialog(
            imageUri =previewImage.value,
            onAccept = {
                when(stringToEnum!!){
                    TypeCamera.DOCUMENT1 -> {
                        viewModel.loadDocument(viewModel.showDialogImagePreview.value!!)
                    }
                    TypeCamera.DEMERIT -> {
                        viewModel.loadImageDemerit(viewModel.showDialogImagePreview.value!!)
                    }
                    TypeCamera.DEFAULT -> {
                        viewModel.loadImages(viewModel.showDialogImagePreview.value!!)
                    }
                }
                viewModel.accessCamera(false)
                navigate()
                viewModel.setShowDialogImagePreview(null)
            },
            onDecline = {
                viewModel.setShowDialogImagePreview(null)
                viewModel.buttonCameraEnabled(true)
                viewModel.changeLoading()
            },
            onDismiss = {
                Toast.makeText(context, "Seleccione una opcion", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CameraPreview(
                cameraController = cameraController,
                lifeCycleOwner = lifeCycleOwner,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {
                when(stringToEnum!!){
                    TypeCamera.DOCUMENT1 -> {
                        CameraScanDataVehicleComplete(
                            showDialog = showModal,
                            onDismiss = { showModal = false },
                            onAccept = { showModal = false }
                        )

                        if(showFirstImage){
                            Image(
                                painter = painterResource(id = R.drawable.guia1_certificado1),
                                contentDescription = null,
                                modifier = Modifier.padding(bottom = 80.dp, top = 25.dp).fillMaxSize().align(Alignment.Center),
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.guia2_certificado1),
                                contentDescription = null,
                                modifier = Modifier.padding(bottom = 80.dp, top = 25.dp).fillMaxSize().align(Alignment.Center),
                            )
                        }

                    }
                    TypeCamera.DEMERIT -> {}
                    TypeCamera.DEFAULT -> {}
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        viewModel.changeLoading()
                        viewModel.buttonCameraEnabled(false)
                        takePhoto(
                            context = context,
                            cameraController = cameraController,
                            showDialog = { viewModel.setShowDialogImagePreview(it) },
                            fileName = typeCamera
                        )
                    },
                    modifier = Modifier.defaultMinSize(minWidth = 56.dp, minHeight = 56.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = greenIcon, disabledContainerColor = bottomBarSelectedItemColor, disabledContentColor = Color.White),
                    enabled = isButtonEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "take a photo",
                    )
                }
            }
            if (isLoading.value)
                Loading(Color.Black)
        }
    }

    // Maneja la presión del botón de retroceso
    BackHandler(onBack = {
        viewModel.accessCamera(false)
        navigate()
        visibilityBar(true)
    })
}