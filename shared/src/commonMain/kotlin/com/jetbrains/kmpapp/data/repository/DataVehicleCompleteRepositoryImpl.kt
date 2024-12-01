package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.interfaces.DataVehicleCompleteDataSourceInterface
import com.jetbrains.kmpapp.data.mappers.toModel
import com.jetbrains.kmpapp.domain.interfaces.DataVehicleCompleteRepositoryInterface
import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.DataVehicleComplete

class DataVehicleCompleteRepositoryImpl(private val datasourse : DataVehicleCompleteDataSourceInterface):
    DataVehicleCompleteRepositoryInterface {

    override suspend fun getVehicleComplete( image: Image, token: String ): StatusResult<DataVehicleComplete> {
        return when (val result = datasourse.getVehicleComplete(image, token)) {
            is StatusResult.Success -> {
                StatusResult.Success(result.value.toModel())
            }
            is StatusResult.Error -> {
                StatusResult.Error(result.message)
            }
        }
    }
}