package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.interfaces.LoginDataSourceLocalInterface
import com.jetbrains.kmpapp.data.interfaces.LoginDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.models.UserDTO
import com.jetbrains.kmpapp.domain.interfaces.LoginRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LoginRepository(
    private val dataSourceRemote: LoginDataSourceRemoteInterface,
    private val dataSourceLocal: LoginDataSourceLocalInterface
) : LoginRepositoryInterface {
    override suspend fun login(userDTO: UserDTO): StatusResult<User> {
        return when (val response = dataSourceRemote.login(userDTO)) {
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> {
                dataSourceLocal.delete().let {
                    dataSourceLocal.insert(Json.encodeToString(response.value))
                }
                StatusResult.Success(response.value)
            }
        }
    }

    override suspend fun requestLocalSession(): StatusResult<User> {
        return when (val response = dataSourceLocal.getAll()) {
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> {
                if (response.value?.isNotEmpty() == true) {
                    val json = Json.decodeFromString<User>(response.value[0].user)
                   return StatusResult.Success(json)
                }
                return StatusResult.Error("No hay usuario precargado")
            }
        }
    }
}