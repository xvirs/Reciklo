package com.jetbrains.kmpapp.data.datasource.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jetbrains.kmpapp.data.interfaces.ListProductLocalInterface
import com.reciklo.db.ListLocalProduct
import com.reciklo.db.RecikloDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.firstOrNull

class ListProductDataSourceLocal(db: RecikloDataBase) : ListProductLocalInterface {
    private val queriesProduct = db.listLocalProductQueries
    override suspend fun insertProduct(product: String) {
        queriesProduct.insert(product)
    }

    override suspend fun getAllProducts(): List<ListLocalProduct> {
        val result = queriesProduct.getAll().asFlow().mapToList(Dispatchers.IO).firstOrNull()
        return result ?: emptyList()
    }

    override suspend fun deleteProduct() {
        queriesProduct.delete()
    }
}