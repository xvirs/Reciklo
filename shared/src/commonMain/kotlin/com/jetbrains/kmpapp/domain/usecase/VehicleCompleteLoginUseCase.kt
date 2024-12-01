package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.data.models.UserDTO
import com.jetbrains.kmpapp.domain.interfaces.VehicleCompleteLoginRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.UserVehicleComplete

class VehicleCompleteLoginUseCase(private val repository: VehicleCompleteLoginRepositoryInterface) {
    suspend fun login(username: String, password: String): StatusResult<UserVehicleComplete> {
        return when (val response = repository.login(userDTO = UserDTO(username, password))) {
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
    }
}