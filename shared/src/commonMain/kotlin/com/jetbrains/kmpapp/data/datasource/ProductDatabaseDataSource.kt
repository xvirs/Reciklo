package com.jetbrains.kmpapp.data.datasource

import com.jetbrains.kmpapp.data.interfaces.ProductDataSourceInterface
import com.jetbrains.kmpapp.domain.models.Product
import com.jetbrains.kmpapp.domain.models.StatusResult


class ProductDatabaseDataSource(): ProductDataSourceInterface {
    override suspend fun getProduct(): StatusResult<List<Product>> {
        TODO("Not yet implemented")
    }
}