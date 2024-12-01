package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.Product
import com.jetbrains.kmpapp.domain.models.ProductResponse
import com.jetbrains.kmpapp.domain.models.StatusResult

interface ProductRepositoryInterface {
    suspend fun postProduct(product: Product, token : String, listImages: List<Image>): StatusResult<ProductResponse>
    suspend fun insertProduct(product: Product)
    suspend fun getAllProducts(): List<Product>
    suspend fun deleteProduct()
    suspend fun getProduct():StatusResult<List<Product>>
    suspend fun saveRemoteProduct(product: Product, token: String, listImages: List<Image>): StatusResult<ProductResponse>
    suspend fun saveLocalProduct(product: Product)
}