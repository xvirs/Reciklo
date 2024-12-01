package com.jetbrains.kmpapp.component.addProductDataScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.colors.grayChip
import com.jetbrains.kmpapp.colors.greenIcon
import kotlinx.coroutines.launch
import com.jetbrains.kmpapp.component.addProductDataScreen.galleryImageList.ItemCardGallery
import com.jetbrains.kmpapp.screens.addProduct.AddProductViewModel
import com.jetbrains.kmpapp.utils.openSingularPickerGallery


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Demeritos(viewModel: AddProductViewModel, changeCamera:(Boolean)->Unit) {

    var isEnabled by remember { mutableStateOf(false) }

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    var text by rememberSaveable { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val uri = viewModel.uriPhoto.collectAsState()

    val singularPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.onSelectImageToBitmap(it)
            }
        })

    LaunchedEffect(viewModel.uriPhoto, text) {
        viewModel.uriPhoto.collect {
            isEnabled = it != null && text.isNotEmpty()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Transparent)
    ) {
        Text(
            text = "Demeritos",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showBottomSheet = true
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cargar un defecto del producto",
                color = Color(0xFF006D43),
                modifier = Modifier.padding(start = 2.dp)
            )
            Icon(Icons.Filled.Add, "Localized description", tint = Color(0xFF006D43))
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Demeritos",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                    )

                    Button(
                        onClick = {
                            viewModel.createFailureProduct(text)
                            text = ""
                            viewModel.deleteUriFailure(null)
                            isEnabled = false
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = greenIcon),
                        enabled = isEnabled
                    ) {
                        Text("Listo")
                    }
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Describí las fallas del producto") },
                    maxLines = 7,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = greenIcon,
                        unfocusedBorderColor = grayChip,
                        focusedPlaceholderColor = greenIcon,
                        unfocusedPlaceholderColor = grayChip,
                        focusedLabelColor = greenIcon) ,
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (uri.value == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                openSingularPickerGallery(singularPickerLauncher)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color(0xFF006D43)
                            ),
                            shape = RoundedCornerShape(50), // Añade bordes redondeados
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 12.dp)
                                .border(0.2.dp,Color(0xFF006D43), RoundedCornerShape(50))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    "Localized description",
                                    tint = Color(0xFF006D43)
                                )

                                Text(
                                    text = "Cargar una imagen",
                                    color = Color(0xFF006D43),
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            changeCamera(true)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF006D43),
                            contentColor = Color.White
                        ),
                    )
                    {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "take photo",
                            tint = Color.White
                        )

                        Text(
                            text = "Toma una foto", modifier = Modifier.padding(start = 2.dp),
                            color = Color.White,
                        )
                    }

                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uri.value != null)
                        ItemCardGallery(uri.value!!, viewModel::deleteUriFailure, {
                            viewModel.setShowDialogImagePreview(it)
                        } ,false, isVisible = true)
                }
            }
        }
    }
}

