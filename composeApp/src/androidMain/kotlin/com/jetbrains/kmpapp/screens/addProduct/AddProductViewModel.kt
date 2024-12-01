package com.jetbrains.kmpapp.screens.addProduct

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.ListCategory
import com.jetbrains.kmpapp.domain.models.ListManufacturer
import com.jetbrains.kmpapp.domain.models.Product
import com.jetbrains.kmpapp.domain.models.ProductFailure
import com.jetbrains.kmpapp.domain.models.Rudac
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StateProduct
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.TypeProduct
import com.jetbrains.kmpapp.domain.models.User
import com.jetbrains.kmpapp.domain.models.VehicleModel
import com.jetbrains.kmpapp.domain.models.VehicleVersion
import com.jetbrains.kmpapp.domain.usecase.CategoryUseCase
import com.jetbrains.kmpapp.domain.usecase.ModelCarUseCase
import com.jetbrains.kmpapp.domain.usecase.ProductUseCase
import com.jetbrains.kmpapp.domain.usecase.RudacUseCase
import com.jetbrains.kmpapp.domain.usecase.TypeProductUseCase
import com.jetbrains.kmpapp.domain.usecase.DataVehicleCompleteUseCase
import com.jetbrains.kmpapp.domain.usecase.VehicleCompleteLoginUseCase
import com.jetbrains.kmpapp.domain.usecase.VehicleModelUseCase
import com.jetbrains.kmpapp.domain.usecase.VehicleVersionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import java.io.ByteArrayOutputStream
import java.io.InputStream

class AddProductViewModel(
    private val useCase: RudacUseCase,
    private val getCategoryUseCase: CategoryUseCase,
    private val getTypeProductUseCase: TypeProductUseCase,
    private val modelUseCase: ModelCarUseCase,
    private val productUseCase: ProductUseCase,
    private val vehicleVersionUseCase:VehicleVersionUseCase,
    private val vehicleModelUseCase: VehicleModelUseCase,
    private val vehicleCompleteLoginUseCase: VehicleCompleteLoginUseCase,
    private val dataVehicleCompleteUseCase: DataVehicleCompleteUseCase,
) : ViewModel() {

    private val _token = MutableStateFlow("")
    val token = _token.asStateFlow()

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap = _bitmap.asStateFlow()

    private val _takePhoto = MutableStateFlow(false)
    val takePhoto = _takePhoto.asStateFlow()

    private val _urisPhotos = MutableStateFlow<List<Uri>>(emptyList())
    val urisPhotos = _urisPhotos.asStateFlow()

    private val _urisDocument = MutableStateFlow<Uri?>(null)
    val urisDocument = _urisDocument.asStateFlow()

    private val _uriPhoto = MutableStateFlow<Uri?>(null)
    val uriPhoto = _uriPhoto.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _buttonTakePhoto = MutableStateFlow(true)
    val buttonTakePhoto = _buttonTakePhoto.asStateFlow()

    private val _isSuccessPhoto = MutableStateFlow(false)
    val isSuccessPhoto = _isSuccessPhoto.asStateFlow()

    private var _rudac: MutableStateFlow<Rudac?> = MutableStateFlow(null)
    val rudac: MutableStateFlow<Rudac?> = _rudac

    private val _flagRudac = MutableStateFlow(false)
    val flagRudac = _flagRudac.asStateFlow()

    private val _flagImage = MutableStateFlow(false)
    val flagImage = _flagImage.asStateFlow()

    private var _resultScanner: MutableStateFlow<String> = MutableStateFlow("")
    val resultScanner: MutableStateFlow<String> = _resultScanner

    private var _nameProduct: MutableStateFlow<String?> = MutableStateFlow("")
    val nameProduct: MutableStateFlow<String?> = _nameProduct

    private var _location: MutableStateFlow<String?> = MutableStateFlow("")
    val location: MutableStateFlow<String?> = _location

    private var _description: MutableStateFlow<String?> = MutableStateFlow("")
    val description: MutableStateFlow<String?> = _description

    private var _model: MutableStateFlow<String?> = MutableStateFlow("")
    val model: MutableStateFlow<String?> = _model

    private var _type: MutableStateFlow<String?> = MutableStateFlow("")
    val type: MutableStateFlow<String?> = _type

    private var _chassis: MutableStateFlow<String?> = MutableStateFlow("")
    val chassis: MutableStateFlow<String?> = _chassis

    private var _year: MutableStateFlow<String?> = MutableStateFlow("")
    val year: MutableStateFlow<String?> = _year

    private var _quantity: MutableStateFlow<String?> = MutableStateFlow("")
    val quantity: MutableStateFlow<String?> = _quantity

    private var _price: MutableStateFlow<String?> = MutableStateFlow("")
    val price: MutableStateFlow<String?> = _price

    private var _height: MutableStateFlow<String?> = MutableStateFlow("")
    val height: MutableStateFlow<String?> = _height

    private var _width: MutableStateFlow<String?> = MutableStateFlow("")
    val width: MutableStateFlow<String?> = _width

    private var _length: MutableStateFlow<String?> = MutableStateFlow("")
    val length: MutableStateFlow<String?> = _length

    private var _weight: MutableStateFlow<String?> = MutableStateFlow("")
    val weight: MutableStateFlow<String?> = _weight

    private var _manufacturerSelected : MutableStateFlow<String> = MutableStateFlow("Selecciona la marca")
    val manufacturerSelected = _manufacturerSelected.asStateFlow()

    private var _categoryProductSelected: MutableStateFlow<String> = MutableStateFlow("Selecciona la categoria")
    val categoryProductSelected = _categoryProductSelected.asStateFlow()

    private var _modelSelected = MutableStateFlow("Selecciona el modelo")
    val modelSelected = _modelSelected.asStateFlow()

    private var _versionSelected = MutableStateFlow("Selecciona la version")
    val versionSelected = _versionSelected.asStateFlow()

    private var _codCategoryProductSelected: MutableStateFlow<Int> = MutableStateFlow(0)
    val codCategoryProductSelected = _codCategoryProductSelected.asStateFlow()

    private var _typeProductSelected: MutableStateFlow<String> = MutableStateFlow("Selecciona el tipo")
    val typeProductSelected = _typeProductSelected.asStateFlow()

    private var _codTypeProductSelected: MutableStateFlow<String> = MutableStateFlow("")
    val codTypeProductSelected = _codTypeProductSelected.asStateFlow()

    private var _codManufacturer: MutableStateFlow<String> = MutableStateFlow("Selecciona la marca")
    val codManufacturer = _codManufacturer.asStateFlow()

    private var _product: MutableStateFlow<Product?> = MutableStateFlow(null)
    val product: MutableStateFlow<Product?> = _product

    private var _listProductFailure = MutableStateFlow<List<ProductFailure>>(emptyList())
    val listProductFailure = _listProductFailure.asStateFlow()

    private val _listCategory = MutableStateFlow<StatusResult<ListCategory>?>(null)
    val listCategory = _listCategory.asStateFlow()

    private val _typeProduct = MutableStateFlow<StatusResult<TypeProduct>?>(null)
    val typeProduct = _typeProduct.asStateFlow()

    private var _listProductFailureDAO = MutableStateFlow<List<ProductFailureDAO>>(emptyList())
    val listProductFailureDAO = _listProductFailureDAO.asStateFlow()

    private var _listProductFailureUri = MutableStateFlow<List<ProductFailure>>(emptyList())
    val listProductFailureUri = _listProductFailureUri.asStateFlow()

    private val _listManufacturer = MutableStateFlow<StatusResult<ListManufacturer>?>(null)
    val listManufacturer = _listManufacturer.asStateFlow()

    private val _listVehicleVersion = MutableStateFlow<StatusResult<VehicleVersion>?>(null)
    val listVehicleVersion = _listVehicleVersion.asStateFlow()

    private val _listVehicleModel = MutableStateFlow<StatusResult<VehicleModel>?>(null)
    val listVehicleModel = _listVehicleModel.asStateFlow()

    private val _listManufacturerFiltered = MutableStateFlow<StatusResult<ListManufacturer>?>(null)
    val listManufacturerFiltered = _listManufacturerFiltered.asStateFlow()

    private val _saveProductTyoe: MutableStateFlow<ProductType> = MutableStateFlow(ProductType.REPLACEMENT)
    val saveProductTyoe = _saveProductTyoe.asStateFlow()

    private val _isButtonPressed  = MutableStateFlow(false)
    val isButtonPressed  = _isButtonPressed .asStateFlow()

    private val _stateOfData = MutableStateFlow(false)
    val stateOfData = _stateOfData.asStateFlow()

    private val _showDialogImagePreview = MutableStateFlow<Uri?>(null)
    val showDialogImagePreview = _showDialogImagePreview.asStateFlow()

    private val _vehicleCompleteToken = MutableStateFlow<String?>(null)
    val vehicleCompleteToken = _vehicleCompleteToken.asStateFlow()

    private var searchJob: Job? = null

    init {
        loginVehicleComplete()
    }
    fun setShowDialogImagePreview(uri : Uri?){
        if(uri != null){
            _showDialogImagePreview.value = uri
        } else {
            _showDialogImagePreview.value = null
        }

    }

    fun resultScanner(cod: String) {
        _resultScanner.value = cod
    }

    fun setProductType(value: ProductType) {
        _saveProductTyoe.value = value
    }

    fun setButtonPressed(value: Boolean) {
        _isButtonPressed .value = value
    }

    fun setToken(value: String) {
        _token.value = value
    }

    fun setFlagRudac(status: Boolean) {
        _flagRudac.value = status
    }

    fun accessCamera(status: Boolean) {
        _takePhoto.value = status
    }

    fun onSelectImages(uri: Uri) {
        _urisPhotos.value += uri
    }

    fun onSelectDocuments(uri: Uri?) {
        _urisDocument.value = uri
    }

    fun loadImages(uri: Uri){
        onSelectImages(uri)
        buttonCameraEnabled(true)
    }
    fun loadImageDemerit(uri: Uri){
        onSelectImageToBitmap(uri)
        changeLoading()
        buttonCameraEnabled(true)
    }

    fun loadDocument(uri: Uri){
        onSelectDocuments(uri)
        changeLoading()
        buttonCameraEnabled(true)
    }
    fun onSelectImageToBitmap(uri: Uri) {
        _uriPhoto.value = uri
    }

    fun changeLoading() {
        _isLoading.value = !isLoading.value
    }

    fun buttonCameraEnabled(status: Boolean){
        _buttonTakePhoto.value = status
    }

    fun deletePhotoSelected(uri: Uri) {
        _urisPhotos.value = urisPhotos.value.filter {
            it != uri
        }
    }

    fun setNameProduct(cod: String) {
        _nameProduct.value = cod
    }

    fun setLocation(cod: String) {
        _location.value = cod
    }

    fun setDescription(cod: String) {
        _description.value = cod
    }

    fun setHeight(cod: String) {
        _height.value = cod
    }

    fun setWidth(cod: String) {
        _width.value = cod
    }

    fun setLength(cod: String) {
        _length.value = cod
    }

    fun setWeight(cod: String) {
        _weight.value = cod
    }

    fun model(cod: String) {
        _model.value = cod
    }

    fun type(cod: String) {
        _type.value = cod
    }

    fun chassis(cod: String) {
        _chassis.value = cod
    }

    fun year(cod: String) {
        _year.value = cod
    }

    fun setTypeProductSelected(value: String) {
        _typeProductSelected.value = value
    }

    fun setCategoryProductSelected(value: String) {
        _categoryProductSelected.value = value
    }

    fun setCodCategoryProductSelected(value: Int) {
        _codCategoryProductSelected.value = value
    }

    fun setManufacturerSelected(value:String){
        _manufacturerSelected.value = value
    }

    fun setVehicleModelSelected(value:String){
        _modelSelected.value = value
    }

    fun setVehicleVersionSelected(value: String){
        _versionSelected.value = value
    }

    fun setCodTypeProductSelected(value: String) {
        _codTypeProductSelected.value = value
    }

    fun setCodManufacturer(value : String){
        _codManufacturer.value = value
    }

    fun quantity(cod: String) {
        _quantity.value = cod
    }

    fun price(cod: String) {
        _price.value = cod
    }

    fun loadRudacData(codRudac : String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = useCase.invoke(codRudac)) {
                is StatusResult.Success -> {
                    _rudac.value = result.value
                    setFlagRudac(true)
                    _isLoading.value = false
                    showToast(context, "Rudac cargado")
                }

                is StatusResult.Error -> {
                    _rudac.value = null
                    setFlagRudac(false)
                    _isLoading.value = false
                    showToast(context, "Rudac no encontrado")
                }
            }
        }
    }

    fun setStateOfData(currentContext: Context, productType: ProductType) : Boolean {
        if (urisPhotos.value.isEmpty()) {
            _stateOfData.value = false
            showToast(currentContext, "Por favor, agrega una imagen.")
            return false
        }
        if( resultScanner.value.isEmpty() && _categoryProductSelected.value != "VEHICULO COMPLETO") {
            _stateOfData.value = false
            showToast(currentContext, "Complete los datos obligatorios.")
            return false
        }
        if( nameProduct.value!!.isEmpty()) {
            _stateOfData.value = false
            showToast(currentContext, "Complete los datos obligatorios.")
            return false
        }
        if( manufacturerSelected.value == "Selecciona la marca") {
            _stateOfData.value = false
            showToast(currentContext, "Por favor, selecciona la marca.")
            return false
        }

        if( year.value!!.isEmpty()) {
            _stateOfData.value = false
            showToast(currentContext, "Complete los datos obligatorios.")
            return false
        }
        when(productType){
            ProductType.REPLACEMENT -> {
                if(categoryProductSelected.value == "Selecciona la categoria") {
                    _stateOfData.value = false
                    showToast(currentContext, "Por favor selecciona la categoria.")
                    return false
                }
            }
            ProductType.VEHICLE -> {}
        }
        when(productType){
            ProductType.REPLACEMENT -> {
                if( quantity.value!!.isEmpty()) {
                    _stateOfData.value = false
                    showToast(currentContext, "Complete los datos obligatorios.")
                    return false
                }
                if( price.value!!.isEmpty()) {
                    _stateOfData.value = false
                    showToast(currentContext, "Complete los datos obligatorios.")
                    return false
                }
                if( height.value!!.isEmpty()) {
                    _stateOfData.value = false
                    showToast(currentContext, "Complete los datos obligatorios.")
                    return false
                }
                if( width.value!!.isEmpty()) {
                    _stateOfData.value = false
                    showToast(currentContext, "Complete los datos obligatorios.")
                    return false
                }
                if( length.value!!.isEmpty()) {
                    _stateOfData.value = false
                    showToast(currentContext, "Complete los datos obligatorios.")
                    return false
                }
                if( weight.value!!.isEmpty()) {
                    _stateOfData.value = false
                    showToast(currentContext, "Complete los datos obligatorios.")
                    return false
                }
            }
            ProductType.VEHICLE -> {}
        }
        when(productType){
            ProductType.REPLACEMENT -> {}
            ProductType.VEHICLE -> {
                if(modelSelected.value == "Selecciona el modelo") {
                    _stateOfData.value = false
                    showToast(currentContext, "Por favor selecciona el modelo.")
                    return false
                }
            }
        }
        if( typeProduct.value == null) {
            _stateOfData.value = false
            showToast(currentContext, "Por favor, selecciona el tipo.")
            return false
        }
        else {
            _stateOfData.value = true
            return true
        }
    }

    fun saveProduct() {
        viewModelScope.launch {
            val product = Product(
                image = urisPhotos.value.map { it.toString() },
                productName = nameProduct.value ?: rudac.value?.descripcionDeLaPieza,
                location = location.value,
                description = description.value,
                numberRUDAC = resultScanner.value,
                marca = manufacturerSelected.value,
                model = model.value,
                type = codTypeProductSelected.value,
                category = codCategoryProductSelected.value,
                chassis = chassis.value,
                year = year.value,
                quantity = quantity.value,
                price = price.value,
                height = height.value,
                width = width.value,
                length = length.value,
                weight = weight.value,
                productsFailure = listProductFailureUri.value
            )
            _product.value = product
        }
    }

    fun loginVehicleComplete() {
        if(vehicleCompleteToken.value.isNullOrEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = vehicleCompleteLoginUseCase.login("admin", "admin")) {
                    is StatusResult.Success -> {
                        _vehicleCompleteToken.value = response.value.access_token
                    }
                    is StatusResult.Error -> {
                        Log.e("Token Vehiculo Completo :","Error")
                    }
                }
            }
        }
    }

    suspend fun infoDocument(context: Context, uri: Uri) {
        _isLoading.value = true
        val defImage = viewModelScope.async( Dispatchers.Default, CoroutineStart.LAZY ) {
            Image(
                image = compressToByteArray(uri, context, 80),
                name = "ImageDocument_${System.currentTimeMillis()}"
            )
        }
         viewModelScope.launch(Dispatchers.IO) {
             when(val response = dataVehicleCompleteUseCase.invoke(defImage.await(), vehicleCompleteToken.value.toString())){
                 is StatusResult.Error -> {
                     withContext(Dispatchers.Main){
                         showToast(context, "Error al obtener la informacion")
                     }
                 }
                 is StatusResult.Success -> {
                     if (response.value.Marca.isNotEmpty()) {
                         setManufacturerSelected(response.value.Marca.trimEnd())
                         val manufacturerCode = searchWithOutSpace(response.value.Marca.trimEnd())
                         fetchManufacturerWithQuery(response.value.Marca.trimEnd())
                         setCodManufacturer(manufacturerCode ?: "")
                         if (response.value.Modelo.isNotEmpty()) {
                             setVehicleModelSelected(response.value.Modelo.trimEnd())
                         }
                     }
                     chassis(response.value.NumeroDeChasis.trimEnd())
                     year(response.value.ANIO.trimEnd())
                 }
             }
             _isLoading.value = false
        }
    }

    private fun searchWithOutSpace(trimEnd: String):String?{
        return listManufacturer.value?.let { statusResult ->
            when (statusResult) {
                is StatusResult.Success -> {
                    statusResult.value.list.find { it.name?.trimEnd() == trimEnd}?.code
                }
                is StatusResult.Error -> {
                    null
                }
                else -> null
            }
        }
    }

    suspend fun saveRemoteProduct(currentContext: Context) {

        _isLoading.value = true

        val product = Product(
            productName = nameProduct.value ?: rudac.value?.descripcionDeLaPieza,
            location = location.value,
            image = urisPhotos.value.map { it.toString() },
            description = description.value,
            numberRUDAC = resultScanner.value,
            marca = if (codManufacturer.value == "Selecciona la marca") "" else codManufacturer.value,
            model = if (modelSelected.value == "Selecciona el modelo") "" else modelSelected.value,
            type = codTypeProductSelected.value,
            category = codCategoryProductSelected.value,
            chassis = chassis.value,
            year = year.value,
            quantity = quantity.value,
            price = price.value,
            height = height.value,
            width = width.value,
            length = length.value,
            weight = weight.value,
            productsFailure = listProductFailureUri.value,
            titlesFailures = listProductFailureUri.value.map { it.failure },
            version = if (versionSelected.value == "Selecciona la version") "" else versionSelected.value,
            tittleMarca = if (manufacturerSelected.value == "Selecciona la marca") "" else manufacturerSelected.value,
        )

        val saveProductsFailureDeferred = viewModelScope.async(Dispatchers.Default, CoroutineStart.LAZY) {
            convertUrisToStringList(
                currentContext,
                product.productsFailure.map { Uri.parse(it.uri) })
        }

        val saveImagesDeferred = viewModelScope.async(Dispatchers.Default, CoroutineStart.LAZY) {
            convertUrisToStringList(
                currentContext,
                product.image.map { Uri.parse(it) }
            )
        }

        val asyncUrisPhotos = viewModelScope.async(Dispatchers.Default, CoroutineStart.LAZY) {
            urisPhotos.value.map { uri ->
                yield()
                Image(
                    image = compressToByteArray(
                        uri,
                        currentContext,
                        80
                    ),
                    name = "ImageProduct_${System.currentTimeMillis()}"
                )

            }
        }

        val asyncListProductFailureUis = viewModelScope.async(Dispatchers.Default, CoroutineStart.LAZY) {
            listProductFailureUri.value.map { productFailure ->
                yield()
                compressToByteArray(
                    productFailure.uri.toUri(),
                    currentContext,
                    80
                ).let { byteArray ->
                    Image(byteArray, "ImageFailure_${System.currentTimeMillis()}")
                }
            }
        }
        val result = productUseCase.postProduct(
            product = product,
            token = token.value,
            listImages = asyncUrisPhotos.await() + asyncListProductFailureUis.await()
        )
        val deferredImages = saveImagesDeferred.await()
        val deferredDemerit = saveProductsFailureDeferred.await()

        when (result) {
            is StatusResult.Success -> {
                product.state = StateProduct.SYNCED
                product.image = listOf(deferredImages[0])
                _isLoading.value = false
                withContext(Dispatchers.Main) {
                    showToast(currentContext, "Alta de producto exitosa.")
                }
            }

            is StatusResult.Error -> {
                when (result.errorType) {
                    StatusResult.ErrorType.NETWORK -> {
                        product.state = StateProduct.FAILED_NETWORK
                        product.image = deferredImages + deferredDemerit
                        productUseCase.insertProduct(product)
                        _isLoading.value = false
                        withContext(Dispatchers.Main) {
                            showToast(currentContext, "Error: Verifique su conexión a internet.")
                        }
                    }

                    StatusResult.ErrorType.SERVER -> {
                        product.state = StateProduct.FAILED_SERVER
                        product.image = deferredImages + deferredDemerit
                        productUseCase.insertProduct(product)
                        _isLoading.value = false
                        withContext(Dispatchers.Main) {
                            showToast(currentContext, "Error: Problema con el servidor.")
                        }
                    }

                    StatusResult.ErrorType.UNKNOWN -> {
                        _isLoading.value = false
                        withContext(Dispatchers.Main) {
                            showToast(currentContext, "Error: Algo salía mal.")
                        }
                    }

                    else -> {}
                }
            }
        }
        resetViewModel()
    }

    private fun showToast(currentContext: Context, value: String ) {
        Toast.makeText(currentContext, value, Toast.LENGTH_SHORT).show()
    }


    fun extractRotation(uri:Uri, context: Context ):Float{
        val inputStream: InputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("No se pudo abrir el URI")

        val exif = ExifInterface(inputStream)

        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val rotationAngle = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f  // Sin rotación
        }

        return rotationAngle
    }

    private fun createBitmapWithRotation(uri:Uri, context: Context):Bitmap{
        val rotationAngle = extractRotation(uri, context)
        val inputStream: InputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("No se pudo abrir el URI")
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        return  if (rotationAngle != 0f) {
            val matrix = Matrix().apply {
                postRotate(rotationAngle)
            }
            val rotateBitmap = Bitmap.createBitmap(
                originalBitmap,
                0,
                0,
                originalBitmap.width,
                originalBitmap.height,
                matrix,
                true
            )
            rotateBitmap
        } else {
            originalBitmap
        }
    }

    private fun compressToByteArray(uri: Uri, context: Context, quality: Int): ByteArray {

        val bitmap = createBitmapWithRotation(uri, context)

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()

    }

    private fun convertUrisToStringList(context: Context, uris: List<Uri>): List<String> {
        return uris.map { uri ->
            val filename = "image_${System.currentTimeMillis()}"

            val rotateBitmap = createBitmapWithRotation(uri, context)

            saveImageToInternalStorage(context, rotateBitmap, filename)

            filename // Guarda solo el nombre del archivo
        }
    }

    private fun saveImageToInternalStorage(context: Context, bitmap: Bitmap?, filename: String) {
        val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, fos)
        fos.close()
    }

    fun createFailureProduct(description: String) {
        if (uriPhoto.value != null) {
            val filename = "image_${System.currentTimeMillis()}"
            //_listProductFailureDAO.value += ProductFailureDAO(description, bitmap.value!!)
            _listProductFailure.value += ProductFailure(description, filename)
            _listProductFailureUri.value += ProductFailure(description, uriPhoto.value.toString())
        }
    }

    fun deleteUriFailure(uri: Uri?) {
        _uriPhoto.value = null
        _bitmap.value = null
    }

    fun stateSavePhoto() {
        _isSuccessPhoto.value = !isSuccessPhoto.value
    }

    fun saveSuccessPhoto(context: Context) {
        Toast.makeText(
            context,
            "Guardado con exito",
            Toast.LENGTH_SHORT
        ).show()

        stateSavePhoto()
    }

    fun resetViewModel() {
        _resultScanner.value = ""
        _nameProduct.value = ""
        _location.value = ""
        _chassis.value = ""
        _year.value = ""
        _quantity.value = ""
        _length.value = ""
        _width.value = ""
        _height.value = ""
        _weight.value = ""
        _price.value = ""
        _description.value = ""
        _manufacturerSelected.value = "Selecciona la marca"
        _typeProductSelected.value = "Selecciona el tipo"
        _categoryProductSelected.value = "Selecciona la categoria"
        _modelSelected.value ="Selecciona el modelo"
        _versionSelected.value = "Selecciona la version"
        _isButtonPressed.value = false
        _stateOfData.value = false
        _bitmap.value = null
        _takePhoto.value = false
        _urisPhotos.value = emptyList()
        _uriPhoto.value = null
        _isLoading.value = false
        _isSuccessPhoto.value = false
        _rudac.value = null
        _flagRudac.value = false
        _model.value = null
        _type.value = null
        _product.value = null
        _listProductFailureDAO.value = emptyList()
        _listProductFailure.value = emptyList()
        _listProductFailureUri.value = emptyList()
        _saveProductTyoe.value = ProductType.REPLACEMENT
        _vehicleCompleteToken.value = null
        cleanVehicleVersion()
        cleanVehicleModel()
    }

    fun fetchCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = getCategoryUseCase.invoke()) {
                is StatusResult.Error -> _listCategory.value = StatusResult.Error(response.message)
                is StatusResult.Success -> _listCategory.value =
                    StatusResult.Success(response.value)
            }
        }
    }

    fun fetchTypeProduct(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = getTypeProductUseCase.invoke(token)) {
                is StatusResult.Error -> _typeProduct.value = StatusResult.Error(response.message)
                is StatusResult.Success -> _typeProduct.value = StatusResult.Success(response.value)
                else -> {}
            }
        }
    }

    fun fetchManufacturerCar(statusSession:SessionManager<User>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = modelUseCase.invoke(statusSession)) {
                is StatusResult.Error -> _listManufacturer.value =
                    StatusResult.Error(response.message)

                is StatusResult.Success -> _listManufacturer.value =
                    StatusResult.Success(response.value)
            }
        }
    }

    fun searchManufacturerLocalWithQuery(query: String) {
        val filteredList = when (listManufacturer.value) {
            is StatusResult.Error -> StatusResult.Error("error")
            is StatusResult.Success -> {
                val manufacturers = (listManufacturer.value as StatusResult.Success<ListManufacturer>).value.list.filter { manufacturer ->
                    manufacturer.name!!.contains(query, ignoreCase = true)
                }
                if (manufacturers.isEmpty()) {
                    StatusResult.Error("error")
                } else {
                    StatusResult.Success(ListManufacturer(manufacturers))
                }
            }
            null -> StatusResult.Success(ListManufacturer(listOf()))
        }
        _listManufacturerFiltered.value = filteredList
    }

    fun fetchManufacturerWithQuery(query: String){

        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(1000L)
            val response = modelUseCase.getForQuery(query = query)
            _listManufacturerFiltered.value = when (response) {
                is StatusResult.Error -> StatusResult.Error(response.message)
                is StatusResult.Success -> {
                    if (response.value.list.isEmpty()) {
                        StatusResult.Error("error")
                    } else {
                        StatusResult.Success(response.value)
                    }
                }
            }
        }
    }

    fun fetchVehicleVersion(token:String, model:Int){
        viewModelScope.launch(Dispatchers.IO) {
            when(val response =vehicleVersionUseCase.invoke(token, model) ){
                is StatusResult.Error -> {_listVehicleVersion.value = StatusResult.Error(response.message)}
                is StatusResult.Success -> {_listVehicleVersion.value = StatusResult.Success(response.value)}
            }
        }
    }

    fun fetchVehicleModel(token:String, model:Int){
        viewModelScope.launch(Dispatchers.IO) {
            when(val response = vehicleModelUseCase.invoke(token, model) ){
                is StatusResult.Error -> {_listVehicleModel.value = StatusResult.Error(response.message)}
                is StatusResult.Success -> {_listVehicleModel.value = StatusResult.Success(response.value)}
            }
        }
    }

    fun cleanVehicleVersion(){
        _listVehicleVersion.value = StatusResult.Error("Lista limpia")
    }

    fun cleanVehicleModel(){
        _listVehicleModel.value = StatusResult.Error("Lista Limpia")
    }

    fun cleanListManufacturerFilter(){
        _listManufacturerFiltered.value = StatusResult.Success(ListManufacturer(listOf()))
    }

    fun replaceUris(list: List<Uri>){
        _urisPhotos.value = list
    }

}


data class ProductFailureDAO(
    val description: String,
    val uri: Bitmap
)