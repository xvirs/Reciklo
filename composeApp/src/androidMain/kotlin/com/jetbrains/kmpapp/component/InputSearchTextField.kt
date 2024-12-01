import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay
import com.jetbrains.kmpapp.component.Loading
import com.jetbrains.kmpapp.component.addProductDataScreen.RenderTextField
import com.jetbrains.kmpapp.domain.models.Description
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import com.jetbrains.kmpapp.screens.addProduct.AddProductViewModel

@Composable
fun InputSearchTextField(
    addProductViewModel: AddProductViewModel,
    listManufacturerCollected: List<Description>,
    manufacturer: String,
    onManufacturerSelected: (String) -> Unit,
    isActiveDropdownVehicleModel: (Boolean) -> Unit,
    isActiveDropdownVehicleVersion: (Boolean) -> Unit,
    token: String,
    statusSession: SessionManager<User>
) {
    var active by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf(if (manufacturer == "Selecciona la marca") "" else manufacturer) }
    val listManufacturer = addProductViewModel.listManufacturerFiltered.collectAsState().value

    LaunchedEffect(search) {
        delay(500L)
        if (search.isNotEmpty()) {
            when (statusSession) {
                is SessionManager.Offline -> {
                    addProductViewModel.searchManufacturerLocalWithQuery(query = search)
                }
                is SessionManager.Online -> {
                    addProductViewModel.fetchManufacturerWithQuery(search)
                }
            }
        }
    }
    val manufacturerDocument = addProductViewModel.manufacturerSelected.collectAsState().value
    if (manufacturer != "Selecciona la marca"){
        LaunchedEffect(manufacturerDocument) {
            search = manufacturer
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        RenderTextField(
            value = search,
            textChange = {
                search = it
                active = search.isNotEmpty()
                addProductViewModel.cleanListManufacturerFilter()
            },
            label = "Marca",
            keyboardType = KeyboardType.Text,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            trailingIcon = Icons.Outlined.KeyboardArrowDown
        )

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.3f),
            properties = PopupProperties(focusable = false),
            expanded = active,
            onDismissRequest = { active = false }
        ) {
            when (listManufacturer) {
                is StatusResult.Success -> {
                    if (listManufacturerCollected.isNotEmpty()) {
                        listManufacturerCollected.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    when (statusSession) {
                                        is SessionManager.Offline -> {}
                                        is SessionManager.Online -> {
                                            addProductViewModel.fetchVehicleModel(
                                                token,
                                                it.id!!
                                            )
                                            addProductViewModel.setVehicleModelSelected("Selecciona el modelo")
                                            addProductViewModel.setVehicleVersionSelected("Selecciona la version")
                                            isActiveDropdownVehicleModel(false)
                                            isActiveDropdownVehicleVersion(false)
                                        }
                                    }
                                    search = it.name!!
                                    onManufacturerSelected(it.name!!)
                                    addProductViewModel.setCodManufacturer(it.code!!)
                                    addProductViewModel.cleanVehicleVersion()
                                    active = false
                                }
                            ) {
                                Text(it.name!!)
                            }
                        }
                    } else {
                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            onClick = {}
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Loading()
                            }
                        }
                    }
                }

                is StatusResult.Error -> {
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        onClick = {}
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No pudimos encontrar la marca", fontSize = 18.sp )
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

