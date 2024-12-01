package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.interfaces.VehicleCompleteLoginDataSourceInterface
import com.jetbrains.kmpapp.data.mappers.toModel
import com.jetbrains.kmpapp.data.models.UserDTO
import com.jetbrains.kmpapp.domain.interfaces.VehicleCompleteLoginRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.UserVehicleComplete

class VehicleCompleteLoginRepositoryImpl(
    private val dataSourceRemote: VehicleCompleteLoginDataSourceInterface
) : VehicleCompleteLoginRepositoryInterface {

    override suspend fun login(userDTO: UserDTO): StatusResult<UserVehicleComplete> {
        return when (val result = dataSourceRemote.login(userDTO)) {
            is StatusResult.Success -> StatusResult.Success(result.value.toModel())
            is StatusResult.Error -> StatusResult.Error(result.message)
        }
    }

}