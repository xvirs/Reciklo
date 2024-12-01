package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.VehicleModel

interface VehicleModelRepositoryInterface {
    suspend fun getVehicleModel(token:String, model:Int): StatusResult<VehicleModel>
}