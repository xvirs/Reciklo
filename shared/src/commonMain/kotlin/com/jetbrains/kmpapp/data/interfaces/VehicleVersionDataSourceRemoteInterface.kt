package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.data.models.VehicleVersionDTO

interface VehicleVersionDataSourceRemoteInterface {
    suspend fun getVersion(token:String, model:Int): StatusResult<VehicleVersionDTO>
}