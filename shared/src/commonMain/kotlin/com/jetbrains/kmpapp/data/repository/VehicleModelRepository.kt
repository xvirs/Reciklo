package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.interfaces.VehicleModelDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.mappers.toModel
import com.jetbrains.kmpapp.domain.interfaces.VehicleModelRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.VehicleModel

class VehicleModelRepository(private val dataSource : VehicleModelDataSourceRemoteInterface) :
    VehicleModelRepositoryInterface {
    override suspend fun getVehicleModel(token:String, model:Int): StatusResult<VehicleModel> {
       return when(val response = dataSource.getVehicleModel(token, model)){
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> {
                StatusResult.Success(response.value.toModel())
            }
        }
    }
}