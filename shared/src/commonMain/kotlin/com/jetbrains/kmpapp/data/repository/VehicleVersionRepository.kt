package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.interfaces.VehicleVersionDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.mappers.toModel
import com.jetbrains.kmpapp.domain.interfaces.VehicleVersionRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.VehicleVersion

class VehicleVersionRepository(private val dataSource : VehicleVersionDataSourceRemoteInterface) :
    VehicleVersionRepositoryInterface {
    override suspend fun getVersion(token:String, model:Int): StatusResult<VehicleVersion> {
        //Implementar guardo local
       return when(val response = dataSource.getVersion(token, model)){
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value.toModel())
        }
    }
}