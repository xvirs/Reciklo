package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.ProfileDataSourceInterface
import com.jetbrains.kmpapp.data.models.ProfileDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body

class ProfileDataDataSourceImpl(): ProfileDataSourceInterface{
    override suspend fun getProfile(token: String): StatusResult<ProfileDTO> {
        val response = BaseClient.baseClient.get(
            url ="${BaseClient.BACKEND_URL}/private/user/profile",
            errorMessage = "Fallo al pedir datos del usuario",
            token = token)

        try {
            response.httpResponse.let {
                if (it!= null){
                    return StatusResult.Success(it.body())
                }
                return StatusResult.Error(response.errorMessage)
            }
        }catch (e:Exception){
            return StatusResult.Error(e.message.toString())
        }
    }

}