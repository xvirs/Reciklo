package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import com.jetbrains.kmpapp.data.models.UserDTO


interface LoginRepositoryInterface {
    suspend fun login(userDTO: UserDTO) : StatusResult<User>

    suspend fun requestLocalSession(): StatusResult<User>
}