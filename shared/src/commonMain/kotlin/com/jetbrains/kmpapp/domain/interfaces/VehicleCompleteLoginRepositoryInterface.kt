package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.data.models.UserDTO
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.UserVehicleComplete

interface VehicleCompleteLoginRepositoryInterface {
    suspend fun login(userDTO: UserDTO): StatusResult<UserVehicleComplete>
}