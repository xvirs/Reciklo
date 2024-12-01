package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.ModelCarDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.models.ModelCarDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body

class ModelCarDataSourceRemote() : ModelCarDataSourceRemoteInterface {
    override suspend fun getModelCar(query:String): StatusResult<ModelCarDTO> {
        val response = BaseClient.baseClient.get(
            url = "${BaseClient.BACKEND_URL}/manufacturers",
            errorMessage = "Error al consultar lo modelos de vehiculos",
            token = "",
            query = query
        )
        try {
            response.httpResponse.let {
                if (it != null){
                    return StatusResult.Success(it.body())
                }
                return StatusResult.Error(response.errorMessage)
            }

        }catch (e : Exception){
            return StatusResult.Error(e.message.toString())
        }
    }
}