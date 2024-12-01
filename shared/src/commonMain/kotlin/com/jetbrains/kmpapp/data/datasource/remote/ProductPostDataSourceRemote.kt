package com.jetbrains.kmpapp.data.datasource.remote


import com.jetbrains.kmpapp.data.interfaces.ProductPostDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.models.ProductPostDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.data.network.ErrorType
import com.jetbrains.kmpapp.data.network.HttpStatus
import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.ProductResponse
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProductPostDataSourceRemote() : ProductPostDataSourceRemoteInterface {

    override suspend fun createProduct(productPostDTO : ProductPostDTO, token : String, listImages: List<Image>): StatusResult<ProductResponse> {
        return when (val response: StatusResult<ProductResponse> =
            fetchProduct("/private/product", "Error al crear el producto",  productPostDTO ,listImages,  token)) {
            is StatusResult.Error -> StatusResult.Error(response.message, response.errorType)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
    }
    private suspend inline fun fetchProduct(
        url: String,
        messageError: String,
        productPostDTO: ProductPostDTO,
        listImages: List<Image>,
        token: String
    ): StatusResult<ProductResponse> {
        val json = Json.encodeToString(productPostDTO)
        val httpResult: HttpStatus

        try {
            httpResult = BaseClient.baseClient.postWhitFile(
                BaseClient.BACKEND_URL + url,
                messageError,
                json,
                "images",
                listImages,
                token
            )
        } catch (e: IOException) {
            return StatusResult.Error("Error de red", StatusResult.ErrorType.NETWORK)
        }

        try {

            httpResult.httpResponse.let {
                if (it != null) {
                    return StatusResult.Success(value = it.body())
                }
            }
            return when (httpResult.errorType) {
                ErrorType.NETWORK -> {
                    StatusResult.Error(httpResult.errorMessage, StatusResult.ErrorType.NETWORK)
                }
                ErrorType.SERVER -> {
                    StatusResult.Error(httpResult.errorMessage, StatusResult.ErrorType.SERVER)
                }
                else -> {
                    StatusResult.Error(httpResult.errorMessage, StatusResult.ErrorType.UNKNOWN)
                }
            }

        } catch (e: Exception) {
            return StatusResult.Error("Error desconocido", StatusResult.ErrorType.UNKNOWN)
        }
    }
}