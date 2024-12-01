package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.ProductRepositoryInterface
import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.Product
import com.jetbrains.kmpapp.domain.models.ProductResponse
import com.jetbrains.kmpapp.domain.models.StatusResult


class ProductUseCase (private val repository: ProductRepositoryInterface) {

    suspend fun getAllProducts(): List<Product> {
        return repository.getAllProducts()
    }

    suspend fun deleteProduct() {
        repository.deleteProduct()
    }

    suspend fun postProduct(product: Product, token: String, listImages: List<Image>): StatusResult<ProductResponse> {
        return repository.postProduct(product, token, listImages)
    }

    suspend fun insertProduct(product: Product) {
        repository.insertProduct(product)
    }

    suspend fun saveRemoteProduct(product: Product, token: String, listImages: List<Image>): StatusResult<ProductResponse> {
        return repository.saveRemoteProduct(product, token, listImages)
    }

    suspend fun saveLocalProduct(product: Product) {
        repository.saveLocalProduct(product)
    }
}