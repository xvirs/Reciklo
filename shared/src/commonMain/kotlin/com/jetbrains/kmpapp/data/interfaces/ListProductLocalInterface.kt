package com.jetbrains.kmpapp.data.interfaces

import com.reciklo.db.ListLocalProduct


interface ListProductLocalInterface {
    suspend fun insertProduct(product: String)
    suspend fun getAllProducts(): List<ListLocalProduct>
    suspend fun deleteProduct()
}