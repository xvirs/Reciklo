package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.VehicleVersionDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.models.VehicleVersionDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body

class VehicleVersionDataSourceRemote : VehicleVersionDataSourceRemoteInterface {
    override suspend fun getVersion(token: String, model: Int): StatusResult<VehicleVersionDTO> {
        val response = BaseClient.baseClient.get(BaseClient.BACKEND_URL+"/private/vehicle-version/vehicle-model/$model", "Error al obtener version de vehiculo", token)
        return try {
            response.httpResponse.let {
                if (it != null) {
                    return StatusResult.Success(it.body())
                }
            }
            StatusResult.Error(response.errorMessage)
        } catch (e: Exception) {
            StatusResult.Error(e.message.toString())
        }
    }
}