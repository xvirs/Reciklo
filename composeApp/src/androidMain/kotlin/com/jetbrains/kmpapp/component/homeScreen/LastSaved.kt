package com.jetbrains.kmpapp.component.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.R
import com.jetbrains.kmpapp.colors.textColorDefault
import com.jetbrains.kmpapp.component.TabComponent
import com.jetbrains.kmpapp.domain.models.LatestProductData
import com.jetbrains.kmpapp.screens.stock.StockViewModel
import com.jetbrains.kmpapp.screens.stock.components.AutoPartListSynchronized
import com.jetbrains.kmpapp.screens.stock.components.AutoPartListUnsynchronized


@Composable
fun LastSaved(latestProductSaved: LatestProductData, stockViewModel: StockViewModel) {

    Column {
        Text(
            text = "Ultimos guardados",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = textColorDefault,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp)
        )
        Box(modifier = Modifier.padding(horizontal = 10.dp)) {
            TabComponent(
                primaryTabText = "Sin sincronizar",
                secondaryTabText = "Sincronizado",
                primaryIcon = painterResource(id = R.drawable.icon_sinsincornizar),
                secondaryIcon = painterResource(id = R.drawable.icon_sincronizado),
                onPrimaryTabSelected = { AutoPartListUnsynchronized(stockViewModel, false) },
                onSecondaryTabSelected = { AutoPartListSynchronized(latestProductSaved) }
            )
        }
    }
}