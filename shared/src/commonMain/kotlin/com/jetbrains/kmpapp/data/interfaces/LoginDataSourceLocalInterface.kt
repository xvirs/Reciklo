package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.data.models.UserSerializable

interface LoginDataSourceLocalInterface {
    fun insert(user : String)

    fun delete()

    suspend fun getAll(): StatusResult<List<UserSerializable>?>
}