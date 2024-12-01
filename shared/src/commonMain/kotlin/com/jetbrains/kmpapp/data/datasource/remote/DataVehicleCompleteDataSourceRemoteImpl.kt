package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.DataVehicleCompleteDataSourceInterface
import com.jetbrains.kmpapp.data.models.DataVehicleCompleteDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body

class DataVehicleCompleteDataSourceRemoteImpl() :
    DataVehicleCompleteDataSourceInterface {

    private val PARAM_URL: String = "http://reciklo.artekium.com:9000/upload-image"
    val baseClient: BaseClient = BaseClient()
    override suspend fun getVehicleComplete( image: Image, token: String ): StatusResult<DataVehicleCompleteDTO> {
        val errorMesage = "Error al pedir info de documentacion 1"
        return when (val response = fetch(PARAM_URL, errorMesage, listOf(image), token)) {
            is StatusResult.Error -> StatusResult.Error(response.message, response.errorType)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
    }

    private suspend fun fetch(
        url: String,
        errorMessage: String,
        image: List<Image>,
        token: String = ""
    ): StatusResult<DataVehicleCompleteDTO> {
        val response = BaseClient
            .baseClient.postWhitFile(
                url,
                errorMessage,
                null,
                "image_file",
                image,
                token)
        try {
            response.httpResponse.let {
                if (it != null) {
                    return StatusResult.Success(value = it.body())
                }
                return StatusResult.Error("Error desconocido", StatusResult.ErrorType.UNKNOWN)
            }
        } catch (e: Exception) {
            return StatusResult.Error("Error desconocido", StatusResult.ErrorType.UNKNOWN)
        }
    }
}