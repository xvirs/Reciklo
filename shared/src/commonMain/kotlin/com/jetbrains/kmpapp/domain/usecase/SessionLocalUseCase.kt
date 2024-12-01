package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.LoginRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User

class SessionLocalUseCase(private val dataSource: LoginRepositoryInterface) {

    suspend fun invoke(): StatusResult<User> {
        return when (val response = dataSource.requestLocalSession()) {
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
    }
}