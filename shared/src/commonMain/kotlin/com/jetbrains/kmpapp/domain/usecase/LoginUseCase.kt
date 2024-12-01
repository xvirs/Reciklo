package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.data.models.UserDTO
import com.jetbrains.kmpapp.domain.interfaces.LoginRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User

class LoginUseCase(private val repository: LoginRepositoryInterface) {

    suspend fun invoke(userDto: UserDTO): StatusResult<User> {
        return when (val response = repository.login(userDTO = userDto)) {
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
    }
}