package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.VehicleCompleteLoginDataSourceInterface
import com.jetbrains.kmpapp.data.models.BodyType
import com.jetbrains.kmpapp.data.models.UserDTO
import com.jetbrains.kmpapp.data.models.UserVehicleCompleteDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body
import io.ktor.http.Parameters
import io.ktor.serialization.JsonConvertException

class VehicleCompleteLoginDataSourceRemoteImpl() : VehicleCompleteLoginDataSourceInterface {
    override suspend fun login(userDTO: UserDTO): StatusResult<UserVehicleCompleteDTO> {
        return when (val response: StatusResult<UserVehicleCompleteDTO> =
            fetchLogin("http://reciklo.artekium.com:9000/token", "error al hacer login", userDTO = userDTO)) {
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
    }

    private suspend inline fun <reified T : Any> fetchLogin(
        url: String,
        messageError: String,
        userDTO: UserDTO
    ): StatusResult<T> {

        val parameters = Parameters.build {
            append("username", userDTO.username)
            append("password", userDTO.password)
        }

        val httpResult = BaseClient.baseClient.post(
            url,
            messageError,
            "",
            "",
            bodyType = BodyType.URLENCODED,
            parameters
        )
        try {
            httpResult.httpResponse?.let {
                val responseBody = it.body<T>()
                return StatusResult.Success(value = responseBody)
            }
            return StatusResult.Error(httpResult.errorMessage)
        } catch (e: JsonConvertException) {
            return StatusResult.Error(e.message.toString())
        }
    }


}