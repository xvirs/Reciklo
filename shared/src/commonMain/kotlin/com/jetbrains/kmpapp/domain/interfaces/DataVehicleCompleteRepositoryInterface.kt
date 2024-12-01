package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.DataVehicleComplete

interface DataVehicleCompleteRepositoryInterface {
    suspend fun getVehicleComplete(image : Image, token : String) : StatusResult<DataVehicleComplete>
}