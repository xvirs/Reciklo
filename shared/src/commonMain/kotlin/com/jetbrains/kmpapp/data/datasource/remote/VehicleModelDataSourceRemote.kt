package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.VehicleModelDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.models.VehicleModelDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body

class VehicleModelDataSourceRemote : VehicleModelDataSourceRemoteInterface {
    override suspend fun getVehicleModel(token:String, model:Int): StatusResult<VehicleModelDTO> {
        val response = BaseClient.baseClient.get(BaseClient.BACKEND_URL+"/private/vehicle-model/by-manufacturer/$model", "Error al consultar modelos de vechiculo", token)
        return try {
            response.httpResponse.let {
                if (it != null) {
                    return StatusResult.Success(it.body())
                }
            }
            StatusResult.Error(response.errorMessage)
        } catch (e: Exception) {
            StatusResult.Error(e.message!!)
        }
    }
}