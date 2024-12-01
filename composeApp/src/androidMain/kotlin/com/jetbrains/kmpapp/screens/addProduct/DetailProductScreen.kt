package com.jetbrains.kmpapp.screens.addProduct
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.jetbrains.kmpapp.component.addProductDataScreen.galleryImageList.ItemCardGallery
import com.jetbrains.kmpapp.component.addProductDataScreen.demerit.ItemFailureProduct
import com.jetbrains.kmpapp.component.ButtonNextStep
import com.jetbrains.kmpapp.component.Loading
import com.jetbrains.kmpapp.component.addProductDataScreen.ProductDetail
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.User
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import com.jetbrains.kmpapp.screens.addProduct.components.ImagePreviewDialog
import org.koin.androidx.compose.get

@Composable
fun DetailProductScreen(
    viewModel: AddProductViewModel,
    navigate:()->Unit
) {
    val listUri = viewModel.urisPhotos.collectAsState()
    val listFailure = viewModel.listProductFailureUri.collectAsState()
    val product = viewModel.product.collectAsState()
    val sessionManager: SessionManagerUseCase = get()
    val session = sessionManager.session.collectAsState()
    val loading = viewModel.isLoading.collectAsState()
    val description = viewModel.description.collectAsState()

    val previewImage = viewModel.showDialogImagePreview.collectAsState()

    if(previewImage.value != null){
        ImagePreviewDialog(
            imageUri =previewImage.value,
            onAccept = null,
            onDecline = null,
            onDismiss = {
                viewModel.setShowDialogImagePreview(null)
            }
        )
    }

    LaunchedEffect(session) {
        when (session.value) {
            is SessionManager.Offline -> {}
            is SessionManager.Online -> viewModel.setToken((session.value as SessionManager.Online<User>).value.token)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp, top = 20.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Revisá tus datos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W300,
                    textAlign = TextAlign.Center
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Titulo del producto: ${product.value?.productName} "
                )
            }
        }
        item {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {

                listUri.value.onEach { photo ->
                    ItemCardGallery(
                        uri = photo,
                        deleteSelected = {},
                        showPreview = {
                            viewModel.setShowDialogImagePreview(it)
                        },
                        presentation = true,
                        isVisible = false
                    )
                }
            }
        }
        item {
            ProductDetail(viewModel)
        }
        item {
            if (listFailure.value.isNotEmpty()) Column(modifier = Modifier.fillMaxWidth()) {
                listFailure.value.onEach {
                    ItemFailureProduct(description = it.failure, uri = it.uri.toUri())
                }
            }
        }
        item {
            OutlinedTextField(
                value = description.value!!,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color(0xFFFBFBFB),
                    disabledTextColor = Color(0xFF171D19)
                ),
                onValueChange = { viewModel.setDescription(it) },
                label = { Text("Descripcion del producto") },
                enabled = false,
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            ButtonNextStep(
                nextStep = {
                    navigate()
                },
                text = "Listo",
                enabled = loading
            )
        }
    }
    if (loading.value) {
        Loading()
    }
}