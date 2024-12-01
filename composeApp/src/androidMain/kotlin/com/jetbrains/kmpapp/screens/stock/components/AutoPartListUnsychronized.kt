package com.jetbrains.kmpapp.screens.stock.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.colors.grayChip
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.jetbrains.kmpapp.component.EmptySpace
import com.jetbrains.kmpapp.component.Loading
import com.jetbrains.kmpapp.component.AutoParteItem
import com.jetbrains.kmpapp.domain.models.ProductToRender
import com.jetbrains.kmpapp.screens.stock.StockViewModel
import org.koin.compose.koinInject

@Composable
fun AutoPartListUnsynchronized(viewModel: StockViewModel, stateFlag: Boolean) {

    val isLoading = viewModel.isLoading.collectAsState()
    val session = koinInject<SessionManagerUseCase>()
    val statusLogin = session.session.collectAsState().value

    val token = when(statusLogin){
        is SessionManager.Offline -> {statusLogin.message}
        is SessionManager.Online -> {statusLogin.value.token}
    }
    val failedProducts = viewModel.failedProductsWithImages.collectAsState()

    var enable by remember { mutableStateOf(true) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadProducts(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn {
            if (failedProducts.value.isNotEmpty()) {
                items(failedProducts.value) { product ->
                    AutoParteItem(
                        ProductToRender(
                            name=product.product.productName?: "",
                            imageUrl = product.image[0].toString(),
                            price =  product.product.price ?: "",
                            manufacturer = product.product.tittleMarca ?: "",
                            rudac = product.product.numberRUDAC ?: ""
                        )
                    )
                    HorizontalDivider(color = grayChip, thickness = 1.dp)

                }
            } else if (isLoading.value.not()) {
                item {
                    Box(modifier = Modifier.fillMaxSize()) {
                        EmptySpace()
                    }
                }
            }

        }

        if (!stateFlag && viewModel.failedProductsWithImages.collectAsState().value.isNotEmpty()) {
            val scope = rememberCoroutineScope()
            ElevatedButtonSyncProducts(
                enabled = enable,
                context = context,
                statusLogin = statusLogin,
                syncProducts = { scope.launch(Dispatchers.IO) {viewModel.syncPendingProducts(token, context) }  }
                ){
                enable = it
            }
        }

        if (isLoading.value) {
            Loading()
        }
    }
}


