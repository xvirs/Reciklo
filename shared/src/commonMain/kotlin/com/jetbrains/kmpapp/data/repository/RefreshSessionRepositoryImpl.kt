package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.interfaces.RefreshSessionDataSourceInterface
import com.jetbrains.kmpapp.domain.interfaces.RefreshSessionInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User

class RefreshSessionRepositoryImpl(private val dataSource: RefreshSessionDataSourceInterface):RefreshSessionInterface {
    override suspend fun refreshToken(token: String): StatusResult<User> {
        return when(val result = dataSource.refreshSession(token)){
            is StatusResult.Error -> StatusResult.Error(result.message)
            is StatusResult.Success -> StatusResult.Success(result.value)
        }
    }
}