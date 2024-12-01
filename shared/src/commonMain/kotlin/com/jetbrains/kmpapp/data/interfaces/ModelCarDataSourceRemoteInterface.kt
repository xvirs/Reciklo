package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.data.models.ModelCarDTO

interface ModelCarDataSourceRemoteInterface {
    suspend fun getModelCar(query:String) : StatusResult<ModelCarDTO>
}