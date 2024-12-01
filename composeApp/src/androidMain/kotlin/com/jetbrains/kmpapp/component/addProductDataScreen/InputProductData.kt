package com.jetbrains.kmpapp.component.addProductDataScreen


import InputSearchTextField
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.colors.greenIcon
import com.jetbrains.kmpapp.component.RenderDropDown
import com.jetbrains.kmpapp.domain.models.ListCategory
import com.jetbrains.kmpapp.domain.models.ListManufacturer
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.TypeProduct
import com.jetbrains.kmpapp.domain.models.VehicleModel
import com.jetbrains.kmpapp.domain.models.VehicleVersion
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import com.jetbrains.kmpapp.screens.addProduct.AddProductViewModel
import com.jetbrains.kmpapp.screens.addProduct.ProductType
import com.jetbrains.kmpapp.utils.TypeRenderDropDown
import kotlinx.coroutines.delay
import org.koin.androidx.compose.get

@Composable
fun InputProductData( viewModel: AddProductViewModel, productType: ProductType) {

    val context = LocalContext.current

    // Declaraciones de Variables
    // Estados para cada campo de datos, inicializados desde el ViewModel
    var numberRudac by rememberSaveable { mutableStateOf(viewModel.resultScanner.value) }
    var nameProduct by rememberSaveable { mutableStateOf(viewModel.nameProduct.value.toString()) }
    var location by rememberSaveable { mutableStateOf(viewModel.location.value.toString()) }
    var chassis by rememberSaveable { mutableStateOf(viewModel.chassis.value.toString()) }
    var year by rememberSaveable { mutableStateOf(viewModel.year.value.toString()) }
    var quantity by rememberSaveable { mutableStateOf(viewModel.quantity.value.toString()) }
    var length by rememberSaveable { mutableStateOf(viewModel.length.value.toString()) }
    var weight by rememberSaveable { mutableStateOf(viewModel.weight.value.toString()) }
    var height by rememberSaveable { mutableStateOf(viewModel.height.value.toString()) }
    var width by rememberSaveable { mutableStateOf(viewModel.width.value.toString()) }
    var price by rememberSaveable { mutableStateOf(viewModel.price.value.toString()) }
    var type by rememberSaveable { mutableStateOf(viewModel.type.value.toString()) }
    var model by rememberSaveable { mutableStateOf(viewModel.model.value.toString()) }

    // Estado para habilitar o deshabilitar campos
    var enabled by rememberSaveable { mutableStateOf(true) }

    // Estado de la sesión y token del usuario
    val session: SessionManagerUseCase = get()
    val statusUser = session.session.collectAsState().value
    var token = ""
    when (statusUser) {
        is SessionManager.Offline -> {}
        is SessionManager.Online -> { token = statusUser.value.token }
    }

    // Estados para los campos seleccionables y dropdowns
    var description by rememberSaveable { mutableStateOf(viewModel.description.value.toString()) }
    var manufacturerSelected by rememberSaveable { mutableStateOf(viewModel.manufacturerSelected.value) }
    var categoryProductSelected by remember { mutableStateOf("") }
    var typeProductSelect by remember { mutableStateOf("") }
    var modelSelected by remember { mutableStateOf("") }
    var versionSelected by remember { mutableStateOf("") }

    // Estados de los dropdowns colectados del ViewModel
    val dropDownCategorySelected = viewModel.categoryProductSelected.collectAsState()
    val dropDownTypeSelected = viewModel.typeProductSelected.collectAsState()
    val dropDownManufacturerSelected = viewModel.manufacturerSelected.collectAsState()
    val dropDownModelSelected = viewModel.modelSelected.collectAsState()
    val dropDownVersionSelected = viewModel.versionSelected.collectAsState()

    // Estados para las listas de datos colectadas del ViewModel
    val listCategory = viewModel.listCategory.collectAsState()
    var listCategoryCollected by remember { mutableStateOf(ListCategory(listOf())) }
    val filteredList = listCategoryCollected.description.filterNot { description ->
        description.name?.contains("VEHICULO COMPLETO", ignoreCase = true) == true
    }
    var listManufacturerCollected by remember { mutableStateOf(ListManufacturer(listOf())) }
    var listModelCollected by remember { mutableStateOf(VehicleModel(listOf())) }
    var listVersionCollected by remember { mutableStateOf(VehicleVersion(listOf())) }
    val listManufacturer = viewModel.listManufacturerFiltered.collectAsState()
    val listTypeProduct = viewModel.typeProduct.collectAsState()
    val listModelVehicle = viewModel.listVehicleModel.collectAsState()
    val listVersionVehicle = viewModel.listVehicleVersion.collectAsState()
    var listTypeProductCollected by remember { mutableStateOf(ListCategory(listOf())) }

    // Indicadores de estado de los dropdowns
    var isActiveDropdownType by remember { mutableStateOf(true) }
    var isActiveDropdownCategory by remember { mutableStateOf(true) }
    var isActiveDropdownManufacturer by remember { mutableStateOf(true) }
    var isActiveDropdownVehicleModel by remember { mutableStateOf(false) }
    var isActiveDropdownVehicleVersion by remember { mutableStateOf(false) }

    // Lógica de Estado
    // Estado de error y validación de datos
    var error by remember { mutableStateOf(false) }
    val stateOfData = viewModel.stateOfData.collectAsState()
    val isButtonPressed = viewModel.isButtonPressed.collectAsState()

    error = if (isButtonPressed.value) {
        !stateOfData.value
    } else {
        false
    }

    var rudacValidation = remember { mutableStateOf(false) }

    // Manejo de datos de RUDAC
    val codRudac by viewModel.resultScanner.collectAsState()
    val rudac by viewModel.rudac.collectAsState()
    val flagRudac by viewModel.flagRudac.collectAsState()

    LaunchedEffect(codRudac, flagRudac) {
        numberRudac = codRudac
        if (flagRudac) {
            numberRudac = codRudac
        }
        nameProduct = rudac?.descripcionDeLaPieza ?: nameProduct
    }

    //Carga de Datos obtenidos al subir documento de vehiculo completo
    val uriDocument = viewModel.urisDocument.collectAsState().value
    when (productType) {
        ProductType.VEHICLE -> {
            if (uriDocument != null){
                LaunchedEffect(uriDocument) {
                    delay(500)
                    uriDocument.let { uri ->
                        viewModel.infoDocument(context = context, uri = uri)
                        viewModel.onSelectDocuments(null)
                    }
                }
            }

        }
        else -> Unit
    }


    // Actualización de otros estados en el ViewModel
    LaunchedEffect(numberRudac) { viewModel.resultScanner(numberRudac) }
    LaunchedEffect(nameProduct) { viewModel.setNameProduct(nameProduct) }
    LaunchedEffect(location) { viewModel.setLocation(location) }
    LaunchedEffect(chassis) { viewModel.chassis(chassis) }
    LaunchedEffect(year) { viewModel.year(year) }
    LaunchedEffect(model) { viewModel.model(model) }
    LaunchedEffect(type) { viewModel.type(type) }
    LaunchedEffect(price) { viewModel.price(price) }
    LaunchedEffect(quantity) { viewModel.quantity(quantity) }
    LaunchedEffect(height) { viewModel.setHeight(height) }
    LaunchedEffect(width) { viewModel.setWidth(width) }
    LaunchedEffect(length) { viewModel.setLength(length) }
    LaunchedEffect(weight) { viewModel.setWeight(weight) }

    // Lógica relacionada con los dropdowns y sus listas
    // Manejo de cambios en los dropdowns de modelo y versión de vehículo
    LaunchedEffect(listModelVehicle.value) {
        when (listModelVehicle.value) {
            is StatusResult.Error -> isActiveDropdownVehicleModel = false
            is StatusResult.Success -> {
                isActiveDropdownVehicleModel = true
                listModelCollected = (listModelVehicle.value as StatusResult.Success<VehicleModel>).value
            }
            null -> {}
        }
    }
    LaunchedEffect(listVersionVehicle.value) {
        when (listVersionVehicle.value) {
            is StatusResult.Error -> isActiveDropdownVehicleVersion = false
            is StatusResult.Success -> {
                isActiveDropdownVehicleVersion = true
                listVersionCollected = (listVersionVehicle.value as StatusResult.Success<VehicleVersion>).value
            }
            null -> {}
        }
    }

    // Actualización de los estados seleccionados de los dropdowns
    LaunchedEffect(dropDownCategorySelected.value) { categoryProductSelected = dropDownCategorySelected.value }
    LaunchedEffect(dropDownTypeSelected.value) { typeProductSelect = dropDownTypeSelected.value }
    LaunchedEffect(dropDownManufacturerSelected.value) { manufacturerSelected = dropDownManufacturerSelected.value }
    LaunchedEffect(dropDownModelSelected.value) { modelSelected = dropDownModelSelected.value }
    LaunchedEffect(dropDownVersionSelected.value) { versionSelected = dropDownVersionSelected.value }

    // Manejo de errores y éxitos al cargar listas desde el ViewModel
    LaunchedEffect(listManufacturer.value) {
        when (listManufacturer.value) {
            is StatusResult.Error -> {
                isActiveDropdownManufacturer = false
                Toast.makeText(context, (listManufacturer.value as StatusResult.Error).message, Toast.LENGTH_SHORT).show()
            }
            is StatusResult.Success -> {
                isActiveDropdownManufacturer = true
                listManufacturerCollected = (listManufacturer.value as StatusResult.Success<ListManufacturer>).value
            }
            null -> {}
        }
    }
    LaunchedEffect(listCategory.value) {
        when (listCategory.value) {
            is StatusResult.Error -> {
                isActiveDropdownCategory = false
                Toast.makeText(context, (listCategory.value as StatusResult.Error).message, Toast.LENGTH_SHORT).show()
            }
            is StatusResult.Success -> {
                listCategoryCollected = (listCategory.value as StatusResult.Success<ListCategory>).value
            }
            null -> {}
        }
    }

    LaunchedEffect(listTypeProduct.value) {
        when (listTypeProduct.value) {
            is StatusResult.Error -> {
                isActiveDropdownType = false
                Toast.makeText(context, (listTypeProduct.value as StatusResult.Error).message, Toast.LENGTH_SHORT).show()
            }
            is StatusResult.Success -> {
                listTypeProductCollected = ListCategory(description = (listTypeProduct.value as StatusResult.Success<TypeProduct>).value.listType.map { it.description })
            }
            null -> {}
        }
    }

    var codeTypeSelected by remember { mutableStateOf("") }

    LaunchedEffect(codeTypeSelected){
        listTypeProduct.value?.let {
            when (it) {
                is StatusResult.Error -> {
                    ""
                }
                is StatusResult.Success -> {
                    it.value.listType.find { type ->
                        if (type.code == codeTypeSelected) {
                            width = type.dimensions?.width?.toString() ?: ""
                            length = type.dimensions?.length?.toString() ?: ""
                            height = type.dimensions?.height?.toString() ?: ""
                            weight =type.dimensions?.weight?.toString() ?: ""
                            true
                        } else {
                            false
                        }
                    }
                }
            }
        }
    }


    // Actualización de los valores en el ViewModel
    LaunchedEffect(description) { viewModel.setDescription(description) }
    LaunchedEffect(typeProductSelect) { viewModel.setTypeProductSelected(typeProductSelect) }
    LaunchedEffect(categoryProductSelected){
        viewModel.setCategoryProductSelected(categoryProductSelected)
        if (categoryProductSelected != "VEHICULO COMPLETO") rudacValidation.value = true  else rudacValidation.value = false
    }
    LaunchedEffect(manufacturerSelected) { viewModel.setManufacturerSelected(manufacturerSelected) }
    LaunchedEffect(modelSelected) { viewModel.setVehicleModelSelected(modelSelected) }
    LaunchedEffect(versionSelected) { viewModel.setVehicleVersionSelected(versionSelected) }


    val chassisDocument = viewModel.chassis.collectAsState().value
    val yearDocument = viewModel.year.collectAsState().value
    val modelDocument = viewModel.model.collectAsState().value

    LaunchedEffect(chassisDocument, yearDocument, modelDocument) {
        chassis = chassisDocument.toString()
        year = yearDocument.toString()
        model = modelDocument.toString()
    }

    when(productType){
        ProductType.REPLACEMENT -> {
            LaunchedEffect(Unit){
                viewModel.setCategoryProductSelected("Selecciona la categoria")
                viewModel.setCodCategoryProductSelected(0)
                viewModel.quantity("")
            }
        }
        ProductType.VEHICLE -> {
            viewModel.setCategoryProductSelected("VEHICULO COMPLETO")
            viewModel.setCodCategoryProductSelected(300)
            viewModel.quantity("1")

            if (listManufacturerCollected.list.isNotEmpty()) {
                listManufacturerCollected.list.forEach {
                    if(it.name == manufacturerSelected){
                        when(statusUser){
                            is SessionManager.Offline -> {}
                            is SessionManager.Online -> {
                                viewModel.fetchVehicleModel(
                                    token,
                                    it.id!!
                                )
                                listModelCollected.list.forEach {
                                    if(it.name == modelSelected){
                                        viewModel.fetchVehicleVersion(token, it.id!!)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    when(productType){
        ProductType.REPLACEMENT-> {
            RenderTextField(
                value = numberRudac,
                textChange = { number ->
                    if (number.all { it.isDigit() }) {
                        numberRudac = number
                    }
                },
                label = "Número de Rudac",
                keyboardType = KeyboardType.Number,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(),
                isError = rudacValidation.value && error
            )
        }
        ProductType.VEHICLE -> {}
    }

    RenderTextField(
        value = nameProduct,
        textChange = { nameProduct = it },
        label = "Nombre del producto",
        keyboardType = KeyboardType.Text,
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth(),
        isError = error && nameProduct.isEmpty()
    )

    RenderTextField(
        value = location,
        textChange = { location = it },
        label = "Locación",
        keyboardType = KeyboardType.Text,
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
    )

    RenderTextField(
        value = chassis,
        textChange = { chassis = it },
        label = "Número de chásis",
        keyboardType = KeyboardType.Number,
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
    )

    InputSearchTextField(
        addProductViewModel = viewModel,
        listManufacturerCollected = listManufacturerCollected.list,
        manufacturer = manufacturerSelected,
        onManufacturerSelected = {manufacturerSelected = it},
        isActiveDropdownVehicleModel = {isActiveDropdownVehicleModel = it},
        isActiveDropdownVehicleVersion = {isActiveDropdownVehicleVersion = it},
        token,
        statusUser
    )

    RenderTextField(
        value = year,
        textChange = { newYear ->
            if (newYear.length <= 4 && newYear.all { it.isDigit() }) {
                year = newYear
            }
        },
        label = "Año",
        keyboardType = KeyboardType.Number,
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth(),
        isError = error && year.isEmpty()
    )

    RenderDropDown(
        typeRenderDropDown = TypeRenderDropDown.MODEL,
        titleDropDown = modelSelected ,
        listData = listModelCollected.list,
        saveInfo ={ typeDropDown, category ->
            when(typeDropDown){
                TypeRenderDropDown.CATEGORY -> {}
                TypeRenderDropDown.TYPE -> {}
                TypeRenderDropDown.MANUFACTURER -> {}
                TypeRenderDropDown.MODEL -> {
                    viewModel.fetchVehicleVersion(token, category.id!!)
                    viewModel.cleanVehicleVersion()
                    modelSelected = category.name!!
                }
                TypeRenderDropDown.VERSION -> {}
            }
        },
        isActive = isActiveDropdownVehicleModel
    )

    RenderDropDown(
        typeRenderDropDown = TypeRenderDropDown.VERSION,
        titleDropDown = versionSelected ,
        listData = listVersionCollected.list,
        saveInfo ={ typeDropDown, category ->
            when(typeDropDown){
                TypeRenderDropDown.CATEGORY -> {}
                TypeRenderDropDown.TYPE -> {}
                TypeRenderDropDown.MANUFACTURER -> {}
                TypeRenderDropDown.MODEL -> {}
                TypeRenderDropDown.VERSION -> {
                    versionSelected = category.name!!
                }
            }

        } ,
        isActive = isActiveDropdownVehicleVersion
    )

    when(productType){
        ProductType.REPLACEMENT -> {
            RenderDropDown(
                typeRenderDropDown = TypeRenderDropDown.CATEGORY,
                titleDropDown = categoryProductSelected,
                listData = filteredList,
                saveInfo = { typeDropDown, category ->
                    when(typeDropDown){
                        TypeRenderDropDown.CATEGORY -> {
                            categoryProductSelected = category.name!!
                            viewModel.setCodCategoryProductSelected(category.id!!)

                        }
                        TypeRenderDropDown.TYPE -> {}
                        TypeRenderDropDown.MANUFACTURER -> {}
                        TypeRenderDropDown.MODEL -> {}
                        TypeRenderDropDown.VERSION -> {}
                    }
                },
                isActive = isActiveDropdownCategory
            )

            RenderDropDown(
                typeRenderDropDown = TypeRenderDropDown.TYPE,
                titleDropDown = typeProductSelect,
                listData = listTypeProductCollected.description,
                saveInfo = { typeDropDown, category ->
                    when(typeDropDown){
                        TypeRenderDropDown.CATEGORY -> {}
                        TypeRenderDropDown.TYPE -> {
                            typeProductSelect = category.name.toString()
                            viewModel.setCodTypeProductSelected(category.code!!)
                            codeTypeSelected = category.code!!
                        }
                        TypeRenderDropDown.MANUFACTURER -> {}
                        TypeRenderDropDown.MODEL -> {}
                        TypeRenderDropDown.VERSION -> {}
                    }
                },
                isActive = isActiveDropdownType
            )
        }
        ProductType.VEHICLE -> {}
    }

    when(productType){
        ProductType.REPLACEMENT -> {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                RenderTextField(
                    value = if(enabled){price} else {"$ ${price.replace(".", "").reversed().chunked(3).joinToString(".").reversed()}"},
                    textChange = { newPrice ->
                        if (newPrice.all { it.isDigit() } && enabled) {
                            price = newPrice
                        }
                    },
                    label = "Precio",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    isError = error && price.isEmpty()
                )
                RenderTextField(
                    value = quantity,
                    textChange = { newQuantity ->
                        if (newQuantity.all { it.isDigit() }) {
                            quantity = newQuantity
                        }
                    },
                    label = "Cantidad",
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    isError = error && quantity.isEmpty()
                )

            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                RenderTextField(
                    value = if(enabled){height} else {"$height cm"},
                    textChange = { newHeight ->
                        if (newHeight.all { it.isDigit() }) {
                            height = newHeight
                        }
                    },
                    label = "Altura",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    isError = error && height.isEmpty()
                )
                RenderTextField(
                    value = if(enabled){width} else {"$width cm"},
                    textChange = { newWidth ->
                        if (newWidth.all { it.isDigit() }) {
                            width = newWidth
                        }
                    },
                    label = "Ancho",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    isError = error && width.isEmpty()
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                RenderTextField(
                    value = if(enabled){length} else {"$length cm"},
                    textChange = { newLength ->
                        if (newLength.all { it.isDigit() }) {
                            length = newLength
                        }
                    },
                    label = "Largo",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    isError = error && length.isEmpty()
                )
                RenderTextField(
                    value = if(enabled){weight} else {"$weight Kg"},
                    textChange = { newWeight ->
                        if (newWeight.all { it.isDigit() }) {
                            weight = newWeight
                        }
                    },
                    label = "Peso",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                    isError = error && weight.isEmpty()
                )

            }

        }
        ProductType.VEHICLE -> {}
    }
    OutlinedTextField(
        value = description,
        onValueChange = { description = it },
        label = { Text("Descripcion del producto") },
        maxLines = 7,
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = greenIcon, focusedLabelColor = greenIcon),
        enabled = enabled
    )
}
