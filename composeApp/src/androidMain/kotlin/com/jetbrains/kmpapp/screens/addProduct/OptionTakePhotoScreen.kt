package com.jetbrains.kmpapp.screens.addProduct

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jetbrains.kmpapp.component.addProductDataScreen.galleryImageList.ListGalleryPhotos
import com.jetbrains.kmpapp.component.ButtonNextStep
import com.jetbrains.kmpapp.navigation.AppScreens
import com.jetbrains.kmpapp.screens.addProduct.components.ImagePreviewDialog
import com.jetbrains.kmpapp.utils.TypeCamera
import com.jetbrains.kmpapp.screens.home.HomeViewModel
import com.jetbrains.kmpapp.utils.CameraUtils
import com.jetbrains.kmpapp.utils.allPermissionsGranted
import com.jetbrains.kmpapp.utils.openMultiplePickerGalleryUseCase


@Composable
fun OptionTakePhotoScreen(navController: NavHostController, navigate:(List<String>)->Unit,homeViewModel:HomeViewModel, visibilityBar:(Boolean)->Unit) {

    val listUris by homeViewModel.urisPhotos.collectAsState()
    val context = LocalContext.current

    val previewImage = homeViewModel.showDialogImagePreview.collectAsState()

    if(previewImage.value != null){
        ImagePreviewDialog(
            imageUri =previewImage.value,
            onAccept = null,
            onDecline = null,
            onDismiss = {
                homeViewModel.setShowDialogImagePreview(null)
            }
        )
    }

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            uris.onEach {
                homeViewModel.onSelectImages(it)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
            text = "Sumá fotos del producto",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            text = "Comenzá agregando fotos generales del producto, utilizando fondo blanco",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )

        CameraSection({
            navController.navigate("camera_screen/$it") {
                navController.previousBackStackEntry
            }
        }, context, type = TypeCamera.DEFAULT, visibilityBar)
        RenderTextGallery { openMultiplePickerGalleryUseCase(multiplePhotoPickerLauncher) }

        ListGalleryPhotos(
            listUris = listUris,
            onSelectImages = homeViewModel::onSelectImages,
            deletePhotoSelected = homeViewModel::deletePhotoSelected,
            setShowDialogImagePreview = homeViewModel::setShowDialogImagePreview)

        if (listUris.isNotEmpty()) {
            ButtonNextStep(
                nextStep = {
                    navigate(listUris.map{it.toString()})
                },
                text = "Listo"
            )
        }
    }

    BackHandler(onBack = {
        navController.navigate(AppScreens.Home.route){
            launchSingleTop = true
        }
        visibilityBar(true)
    })

}


@Composable
fun CameraSection(navigate:(TypeCamera)->Unit,
                  context: Context,
                  type: TypeCamera,
                  visibilityBar:(Boolean)->Unit) {

    var allPermissionsGranted by remember{ mutableStateOf(allPermissionsGranted(context)) }

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){granted ->
        if (granted.all { it.value }){
            allPermissionsGranted = true
        }
    }
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.LightGray, Color.White)
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 20.dp),
        onClick = {
            if (allPermissionsGranted) {
                visibilityBar(false)
                navigate(type)
            } else {
                permissionsLauncher.launch(CameraUtils.REQUIRED_PERMISSIONS)
            }
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    modifier = Modifier.size(60.dp, 60.dp),
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "take a photo"
                )
                Text(
                    text = "Sacar foto",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun RenderTextGallery(openGallery: () -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = "Galeria",
            fontSize = 22.sp,
            fontWeight = FontWeight.W500
        )
        Text(
            modifier = Modifier
                .padding(end = 20.dp)
                .clickable {
                    openGallery()
                },
            text = "Seleccionar de tu galeria",
            fontSize = 16.sp,
            fontWeight = W400,
            color = Color(android.graphics.Color.parseColor("#006D43"))
        )
    }
}