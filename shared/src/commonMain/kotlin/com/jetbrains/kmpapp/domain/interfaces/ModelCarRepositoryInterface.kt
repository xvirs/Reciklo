package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.ListManufacturer
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User

interface ModelCarRepositoryInterface {
    suspend fun getModelCar(statusSession : SessionManager<User>): StatusResult<ListManufacturer>
    suspend fun getModelCarWithQuery(query:String):StatusResult<ListManufacturer>
}