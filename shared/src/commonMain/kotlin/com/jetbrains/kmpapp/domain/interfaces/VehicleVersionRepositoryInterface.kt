package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.VehicleVersion
interface VehicleVersionRepositoryInterface {
    suspend fun getVersion(token:String, model:Int): StatusResult<VehicleVersion>
}