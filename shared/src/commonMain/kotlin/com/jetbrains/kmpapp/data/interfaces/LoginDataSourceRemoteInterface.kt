package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import com.jetbrains.kmpapp.data.models.UserDTO

interface LoginDataSourceRemoteInterface {
    suspend fun login(userDTO: UserDTO): StatusResult<User>
}