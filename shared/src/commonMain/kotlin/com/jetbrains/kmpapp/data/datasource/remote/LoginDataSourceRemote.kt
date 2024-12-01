package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.LoginDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.models.BodyType
import com.jetbrains.kmpapp.data.models.UserDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import io.ktor.client.call.body
import io.ktor.serialization.JsonConvertException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LoginDataSourceRemote() : LoginDataSourceRemoteInterface {
    override suspend fun login(userDTO: UserDTO): StatusResult<User> {
        return when (val response: StatusResult<User> =
            fetchLogin("/private/login", "error al hacer login", userDTO = userDTO)) {
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
    }

    private suspend inline fun <reified T : Any> fetchLogin(
        url: String,
        messageError: String,
        userDTO: UserDTO
    ): StatusResult<T> {

//        Se pasa de Json a string para enviar los datos serializados
        val json = Json.encodeToString(userDTO)

        val httpResult = BaseClient.baseClient.post(
            BaseClient.BACKEND_URL + url,
            messageError,
            json,
            "",
            bodyType = BodyType.RAW
        )
        try {
            httpResult.httpResponse?.let {
                return StatusResult.Success(value = it.body())
            }
            return StatusResult.Error(httpResult.errorMessage)
        } catch (e: JsonConvertException) {
            return StatusResult.Error(e.message.toString())
        }
    }
}