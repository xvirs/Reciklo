package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User

interface RefreshSessionInterface {
    suspend fun refreshToken(token:String):StatusResult<User>
}