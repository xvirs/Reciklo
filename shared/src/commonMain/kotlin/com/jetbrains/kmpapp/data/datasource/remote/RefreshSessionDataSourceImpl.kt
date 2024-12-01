package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.RefreshSessionDataSourceInterface
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import io.ktor.client.call.body

class RefreshSessionDataSourceImpl():RefreshSessionDataSourceInterface {
    override suspend fun refreshSession(token:String): StatusResult<User> {
        val response = BaseClient.baseClient.get(
            url = "${BaseClient.BACKEND_URL}/private/refresh",
            errorMessage = "",
            token = token
        )
        try {
            response.httpResponse.let {
               if (it!=null){
                   return StatusResult.Success(it.body())
               }
                return StatusResult.Error(response.errorMessage)
            }
        }catch (e:Exception){
            return StatusResult.Error(response.errorMessage)
        }
    }
}