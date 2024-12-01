package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.data.models.UserDTO
import com.jetbrains.kmpapp.data.models.UserVehicleCompleteDTO
import com.jetbrains.kmpapp.domain.models.StatusResult

interface VehicleCompleteLoginDataSourceInterface {
    suspend fun login(userDTO: UserDTO): StatusResult<UserVehicleCompleteDTO>
}