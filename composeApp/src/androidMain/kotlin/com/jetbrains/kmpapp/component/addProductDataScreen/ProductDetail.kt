package com.jetbrains.kmpapp.component.addProductDataScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.screens.addProduct.AddProductViewModel
import com.jetbrains.kmpapp.screens.addProduct.ProductType

@Composable
fun ProductDetail(addProductViewModel: AddProductViewModel) {

    val productType = addProductViewModel.saveProductTyoe.collectAsState().value

    val numberRudac = addProductViewModel.resultScanner.collectAsState().value.toString()
    val nameProduct = addProductViewModel.nameProduct.collectAsState().value.toString()
    val location = addProductViewModel.location.collectAsState().value.toString()
    val type = addProductViewModel.type.collectAsState().value.toString()
    val chassis = addProductViewModel.chassis.collectAsState().value.toString()
    val year = addProductViewModel.year.collectAsState().value.toString()
    val quantity = addProductViewModel.quantity.collectAsState().value.toString()
    val price = addProductViewModel.price.collectAsState().value.toString()
    val height = addProductViewModel.height.collectAsState().value.toString()
    val width = addProductViewModel.width.collectAsState().value.toString()
    val length = addProductViewModel.length.collectAsState().value.toString()
    val weight = addProductViewModel.weight.collectAsState().value.toString()
    val category = if(addProductViewModel.categoryProductSelected.collectAsState().value == "Selecciona la categoria") "" else addProductViewModel.categoryProductSelected.collectAsState().value
    val typeSelected = if (addProductViewModel.typeProductSelected.collectAsState().value == "Selecciona el tipo") "" else addProductViewModel.typeProductSelected.collectAsState().value
    val manufacturer = if(addProductViewModel.manufacturerSelected.collectAsState().value == "Selecciona la marca") "" else addProductViewModel.manufacturerSelected.collectAsState().value
    val model = if(addProductViewModel.modelSelected.collectAsState().value == "Selecciona el modelo") "" else addProductViewModel.modelSelected.collectAsState().value
    val version = if(addProductViewModel.versionSelected.collectAsState().value == "Selecciona la version")"" else addProductViewModel.versionSelected.collectAsState().value


    Column(
        Modifier.padding(vertical = 8.dp)
    ) {
        when(productType){
            ProductType.REPLACEMENT -> {
                DisplayField(title = "Número Rudac", value = numberRudac)
            }
            ProductType.VEHICLE -> {}
        }
        DisplayField(title = "Nombre de Producto", value = nameProduct)
        DisplayField(title = "Localización", value = location)
        DisplayField(title = "Num. Chasis", value = chassis)
        DisplayField(title = "Marca", value = manufacturer)
        DisplayField(title = "Año", value = year)
        DisplayField(title = "Modelo", value = model)
        DisplayField(title = "Version", value = version)
        when(productType){
            ProductType.REPLACEMENT -> {
                DisplayField(title = "Categoría", value = category)
//                    Tipo no se carga en ningun lado creo que quedo de una implementacion vieja.
                DisplayField(title = "Tipo", value = typeSelected)
                DisplayField(title = "Precio", value = "$ $price")
                DisplayField(title = "Cantidad", value = quantity)
                DisplayField(title = "Altura", value = "$height cm")
                DisplayField(title = "Ancho", value = "$width cm")
                DisplayField(title = "Largo", value = "$length cm")
                DisplayField(title = "Peso", value = "$weight Kg")
            }
            ProductType.VEHICLE -> {
                DisplayField(title = "Categoría", value = category)
            }
        }
    }
}

@Composable
fun DisplayField(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = if (value.isEmpty()) "-" else value,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f))
    }
}
