package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.LatestProductSavedDataSourceInterface
import com.jetbrains.kmpapp.data.models.LatestProductDataDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body

class LatestProductSavedDataSource(): LatestProductSavedDataSourceInterface {
    override suspend fun getLatestProductSaved(token:String): StatusResult<LatestProductDataDTO> {
        val response = BaseClient.baseClient.get(
            url = BaseClient.BACKEND_URL + "/private/products/latest",
            errorMessage = "Error al consultar ultimos 10 guardados",
            token = token)
        try {
            response.httpResponse.let {
                if(it!=null){
                    return StatusResult.Success(it.body())
                }
            }
             return StatusResult.Error(response.errorMessage)
        }catch (E:Exception){
             return StatusResult.Error("Error en la peticion")
        }
    }
}