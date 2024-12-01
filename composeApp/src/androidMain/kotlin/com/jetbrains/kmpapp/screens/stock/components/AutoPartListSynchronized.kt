package com.jetbrains.kmpapp.screens.stock.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetbrains.kmpapp.colors.grayChip
import com.jetbrains.kmpapp.component.AutoParteItem
import com.jetbrains.kmpapp.component.EmptySpace
import com.jetbrains.kmpapp.domain.models.LatestProductData

@Composable
fun AutoPartListSynchronized(listSynchronizedProducts:LatestProductData) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            if (listSynchronizedProducts.productToRenders.isNotEmpty()) {
                listSynchronizedProducts.productToRenders.onEach {
                    AutoParteItem(it)
                    Divider(modifier = Modifier.height(1.dp).background(grayChip))
                }
            }
            else {
                Box(modifier = Modifier.fillMaxSize()) {
                    EmptySpace()
                }
            }
        }
    }
}