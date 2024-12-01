package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User

interface RefreshSessionDataSourceInterface {

    suspend fun refreshSession(token:String): StatusResult<User>
}