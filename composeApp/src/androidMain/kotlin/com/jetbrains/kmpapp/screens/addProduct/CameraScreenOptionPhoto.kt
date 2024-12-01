package com.jetbrains.kmpapp.screens.addProduct


import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.camera.view.LifecycleCameraController
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.colors.bottomBarSelectedItemColor
import com.jetbrains.kmpapp.colors.greenIcon
import com.jetbrains.kmpapp.component.Loading
import com.jetbrains.kmpapp.component.addProductDataScreen.cameraScreen.CameraPreview
import com.jetbrains.kmpapp.component.addProductDataScreen.cameraScreen.takePhoto
import com.jetbrains.kmpapp.screens.addProduct.components.ImagePreviewDialog
import com.jetbrains.kmpapp.screens.home.HomeViewModel
import com.jetbrains.kmpapp.utils.TypeCamera

@Composable
fun CameraScreenOptionPhoto(typeCamera:String, navigate:() ->Unit, viewModel: HomeViewModel, visibilityBar:(Boolean)->Unit) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val isLoading = viewModel.isLoading.collectAsState()
    val isButtonEnabled by viewModel.buttonTakePhoto.collectAsState()
    val previewImage = viewModel.showDialogImagePreview.collectAsState()

    val stringToEnum : TypeCamera? = enumValues<TypeCamera>().find {
        it.name.equals(typeCamera, ignoreCase = true)
    }

    if(previewImage.value != null){
        ImagePreviewDialog(
            imageUri =previewImage.value,
            onAccept = {
                when(stringToEnum!!){
                    TypeCamera.DOCUMENT1 -> {
                    }
                    TypeCamera.DEMERIT -> {
                        viewModel.loadImageDemerit(viewModel.showDialogImagePreview.value!!)
                    }
                    TypeCamera.DEFAULT -> {
                        viewModel.loadImages(viewModel.showDialogImagePreview.value!!)
                    }
                }
                viewModel.setLoading(false)
                navigate()
                viewModel.setShowDialogImagePreview(null)
            },
            onDecline = {
                viewModel.setShowDialogImagePreview(null)
                viewModel.buttonCameraEnabled(true)
                viewModel.setLoading(false)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        viewModel.setLoading(true)
                        viewModel.buttonCameraEnabled(false)
                        takePhoto(
                            context = context,
                            cameraController = cameraController,
                            showDialog = { viewModel.setShowDialogImagePreview(it) },
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
        navigate()
        visibilityBar(true)
    })
}


