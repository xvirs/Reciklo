package com.jetbrains.kmpapp.screens.stock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.component.TabComponent
import com.jetbrains.kmpapp.domain.models.LatestProductData
import com.jetbrains.kmpapp.domain.models.StatusResult
import org.koin.compose.koinInject
import com.jetbrains.kmpapp.screens.stock.components.AutoPartListSynchronized
import com.jetbrains.kmpapp.screens.stock.components.AutoPartListUnsynchronized

@Composable
fun StockScreen() {
    val stockViewModel = koinInject<StockViewModel>()
    val listCollector = stockViewModel.latestProductSaved.collectAsState()
    var listLatestSaved by remember { mutableStateOf(LatestProductData(listOf())) }
    stockViewModel.initLatestSaved()

    when(val res = listCollector.value){
        is StatusResult.Error -> {}
        is StatusResult.Success -> listLatestSaved = res.value
    }

    TabComponent(
        primaryTabText = "Sin sincronizar",
        secondaryTabText = "Sincronizado",
        primaryIcon = painterResource(id = R.drawable.icon_sinsincornizar),
        secondaryIcon = painterResource(id = R.drawable.icon_sincronizado),
        onPrimaryTabSelected = { AutoPartListUnsynchronized(stockViewModel, false) },
        onSecondaryTabSelected = { AutoPartListSynchronized(listLatestSaved) }
    )
}






