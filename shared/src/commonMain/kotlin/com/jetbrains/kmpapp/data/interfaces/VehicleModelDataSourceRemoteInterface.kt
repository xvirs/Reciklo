package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.data.models.VehicleModelDTO

interface VehicleModelDataSourceRemoteInterface {
    suspend fun getVehicleModel(token:String, model:Int): StatusResult<VehicleModelDTO>
}