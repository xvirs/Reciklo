package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.data.models.DataVehicleCompleteDTO
import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.StatusResult

interface DataVehicleCompleteDataSourceInterface {

    suspend fun getVehicleComplete(image : Image, token : String) : StatusResult<DataVehicleCompleteDTO>

}