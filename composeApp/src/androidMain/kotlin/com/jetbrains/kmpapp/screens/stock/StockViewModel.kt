package com.jetbrains.kmpapp.screens.stock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.LatestProductData
import com.jetbrains.kmpapp.domain.models.Product
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StateProduct
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.usecase.LastProductsSavedUseCase
import com.jetbrains.kmpapp.domain.usecase.ProductUseCase
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class StockViewModel(
    private val productUseCase: ProductUseCase,
    private val latestProductsSavedUseCase : LastProductsSavedUseCase,
    private val sessionManager : SessionManagerUseCase
) : ViewModel() {

    private val _productsWithImages = MutableStateFlow<StatusResult<List<ProductWithImage>>?>(null)
    val productsWithImages = _productsWithImages.asStateFlow()

    private val _syncedProductsWithImages = MutableStateFlow<List<ProductWithImage>>(emptyList())
    val syncedProductsWithImages = _syncedProductsWithImages.asStateFlow()

    private val _failedProductsWithImages = MutableStateFlow<List<ProductWithImage>>(emptyList())
    val failedProductsWithImages = _failedProductsWithImages.asStateFlow()

    private val _latestProductsSaved = MutableStateFlow <StatusResult<LatestProductData>>(StatusResult.Error("Pending sync"))
    val latestProductSaved = _latestProductsSaved.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var isSyncing = false

    fun initLatestSaved(){
        when(val session = sessionManager.session.value){
            is SessionManager.Offline -> {}
            is SessionManager.Online -> {fetchLatestProductSaved(session.value.token)}
        }
    }

    fun fetchLatestProductSaved(token:String){
        viewModelScope.launch {
            when(val response = latestProductsSavedUseCase.invoke(token)){
                is StatusResult.Error -> _latestProductsSaved.value = StatusResult.Error("Error al pedir ultimos guardados")
                is StatusResult.Success -> _latestProductsSaved.value = StatusResult.Success(response.value)
            }
        }
    }


    fun loadProducts(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val listLocalProduct = productUseCase.getAllProducts()

                // Primero, crea los objetos ProductWithImage sin las imágenes
                val productsWithImagesList = listLocalProduct.map { product ->
                    ProductWithImage(product, emptyList())
                }

                // Actualiza _productsWithImages con los productos
                _productsWithImages.value = StatusResult.Success(productsWithImagesList)


                // Luego, carga las imágenes en segundo plano
                productsWithImagesList.forEachIndexed { index, productWithImage ->
                    val images = productWithImage.product.image.mapNotNull { filename ->
                        loadImageFromInternalStorage(context, filename)
                    }

                    // Actualiza el producto con las imágenes cargadas
                    when (_productsWithImages.value) {
                        is StatusResult.Success -> {
                            _productsWithImages.value = StatusResult.Success(
                                (_productsWithImages.value as StatusResult.Success<List<ProductWithImage>>).value.toMutableList()
                                    .apply {
                                        this[index] = productWithImage.copy(image = images)
                                    }

                            )
                        }

                        is StatusResult.Error -> {
                            _productsWithImages.value =
                                StatusResult.Error("No se pudo cargar la imagen")
                        }

                        null -> {}
                    }

                }
                _syncedProductsWithImages.value = when (_productsWithImages.value) {
                    is StatusResult.Error -> {
                        emptyList()
                    }

                    is StatusResult.Success -> (_productsWithImages.value as StatusResult.Success<List<ProductWithImage>>).value.filter { it.product.state == StateProduct.SYNCED }
                    null -> {
                        emptyList()
                    }
                }

                _failedProductsWithImages.value = when (_productsWithImages.value) {
                    is StatusResult.Error -> {
                        emptyList()
                    }

                    is StatusResult.Success -> (_productsWithImages.value as StatusResult.Success<List<ProductWithImage>>).value.filter {
                        it.product.state == StateProduct.FAILED_SERVER || it.product.state == StateProduct.FAILED_NETWORK
                    }

                    null -> {
                        emptyList()
                    }
                }

                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("StockViewModel", "Error al cargar productos", e)
            } finally {
            }
        }
    }

    suspend fun syncPendingProducts(token: String, context: Context) {
        if (!isSyncing) {
            isSyncing = true
            _isLoading.value = true

            val newFailedProductsWithImages = mutableListOf<ProductWithImage>()
            val newSyncedProductsWithImages = mutableListOf<ProductWithImage>()
            val deferredFailed = mutableListOf<Deferred<Int>>()
            val deferredsImage = mutableListOf<Deferred<Int>>()

            Log.e("prueba", "1 async")
            withContext(Dispatchers.IO) {
                // Borra todos los productos existentes
                productUseCase.deleteProduct()
            }

            syncedProductsWithImages.value.forEachIndexed {index, productWithImage ->
                deferredFailed.add(
                    viewModelScope.async {
                        yield()
                        Log.e("prueba", "incia guardo local $index")
                        productUseCase.insertProduct(productWithImage.product)
                        newSyncedProductsWithImages.add(productWithImage)
                        Log.e("prueba", "termina guardo local $index")
                    }
                )
            }

            failedProductsWithImages.value.forEachIndexed { index, productWithImage ->
                Log.e("prueba", "$index async")
                deferredsImage.add(
                    viewModelScope.async(Dispatchers.Default, CoroutineStart.LAZY) {
                        yield()
                        val compressImage = productWithImage.image.map {
                            Image(
                                compressToByteArray(it, context, 80),
                                "ImageFailure_${System.currentTimeMillis()}"
                            )
                        }
                        when (productUseCase.postProduct(
                            productWithImage.product,
                            token,
                            compressImage
                        )) {
                            is StatusResult.Success -> {
                                productWithImage.product.state = StateProduct.SYNCED
                                productWithImage.product.image = listOf(productWithImage.product.image[0])
                                newSyncedProductsWithImages.add(productWithImage)
                                withContext(Dispatchers.Main) {
                                    showToast(context, "Se sincronizaron los productos pendientes")
                                }
                            }

                            is StatusResult.Error -> {
                                productWithImage.product.state = StateProduct.FAILED_SERVER
                                productUseCase.insertProduct(productWithImage.product)
                                newFailedProductsWithImages.add(productWithImage)
                                withContext(Dispatchers.Main) {
                                    showToast(context, "Error al sincronizar los productos pendientes")
                                }
                            }
                        }
                        Log.e("prueba", "finish async $index")
                    }
                )
            }

            Log.e("prueba", "BEFORE AWAIT")
            deferredsImage.awaitAll()
            deferredFailed.awaitAll()
            _isLoading.value = false
            isSyncing = false

            _failedProductsWithImages.value = newFailedProductsWithImages
            _syncedProductsWithImages.value = newSyncedProductsWithImages
            _productsWithImages.value = StatusResult.Success(newSyncedProductsWithImages)
            Log.e("prueba", "FINISH")
        }
    }

    private suspend fun loadImageFromInternalStorage(context: Context, filename: String): Uri? {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, filename)
            if (file.exists()) {
                Uri.fromFile(file)
            } else {
                // El archivo no existe
                null
            }
        }
    }

    private fun compressToByteArray(uri: Uri, context: Context, quality: Int): ByteArray {
        val inputStream: InputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("No se pudo abrir el URI")

        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        val byteArrayOutputStream = ByteArrayOutputStream()
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()

    }

    fun showToast(currentContext: Context, value: String) {
        Toast.makeText(currentContext, value, Toast.LENGTH_SHORT).show()
    }
}

//fun deleteImageFromInternal(context: Context, fileName: String): Boolean {
//    val file = File(context.filesDir, fileName)
//    return file.exists() && file.delete()  // Devuelve `true` si se eliminó correctamente
//}


data class ProductWithImage(val product: Product, val image: List<Uri>)