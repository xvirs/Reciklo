package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.Product
import com.jetbrains.kmpapp.domain.models.StatusResult

interface ProductDataSourceInterface {
    suspend fun getProduct(): StatusResult<List<Product>>
}