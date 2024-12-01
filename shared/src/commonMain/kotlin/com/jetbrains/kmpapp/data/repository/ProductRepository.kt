package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.datasource.local.ListProductDataSourceLocal
import com.jetbrains.kmpapp.data.datasource.remote.ProductPostDataSourceRemote
import com.jetbrains.kmpapp.data.mappers.mapProductToDTO
import com.jetbrains.kmpapp.domain.interfaces.ProductRepositoryInterface
import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.Product
import com.jetbrains.kmpapp.domain.models.ProductResponse
import com.jetbrains.kmpapp.domain.models.StatusResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ProductRepository(
    private val listProductDataSourceLocal: ListProductDataSourceLocal,
    private val postDataSource : ProductPostDataSourceRemote
) : ProductRepositoryInterface {


    private val jsonFormat = Json { ignoreUnknownKeys = true }


    override suspend fun postProduct(
        product: Product,
        token: String,
        listImages: List<Image>
    ): StatusResult<ProductResponse> {
        val productPostDTO = mapProductToDTO(product)
        val response = postDataSource.createProduct(productPostDTO, token, listImages)
        return when (response) {
            is StatusResult.Success -> {
                StatusResult.Success(response.value)
            }

            is StatusResult.Error -> {
                when (response.errorType) {
                    StatusResult.ErrorType.NETWORK -> {
                        StatusResult.Error("Error de red", StatusResult.ErrorType.NETWORK)
                    }

                    StatusResult.ErrorType.SERVER -> {
                        StatusResult.Error("Error del servidor", StatusResult.ErrorType.SERVER)
                    }

                    else -> {
                        StatusResult.Error("Error desconocido", StatusResult.ErrorType.UNKNOWN)
                    }
                }
            }
        }
    }


    override suspend fun insertProduct(product: Product) {
        val productJson = jsonFormat.encodeToString(product)
        listProductDataSourceLocal.insertProduct(productJson)
    }

    override suspend fun getAllProducts(): List<Product> {
        val productsJson = listProductDataSourceLocal.getAllProducts()
        return productsJson.map { jsonFormat.decodeFromString<Product>(it.products ?: "") }
    }

    override suspend fun deleteProduct() {
        listProductDataSourceLocal.deleteProduct()
    }

    override suspend fun getProduct(): StatusResult<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRemoteProduct(product: Product, token: String, listImages: List<Image>): StatusResult<ProductResponse> {
        val productPostDTO = mapProductToDTO(product)
        return when (val response = postDataSource.createProduct(productPostDTO, token, listImages)) {
            is StatusResult.Error -> {
                StatusResult.Error(response.message)
            }
            is StatusResult.Success -> {
                StatusResult.Success(response.value)
            }
        }
    }

    override suspend fun saveLocalProduct(product: Product) {
        val productJson = jsonFormat.encodeToString(product)
        listProductDataSourceLocal.insertProduct(productJson)
    }
}