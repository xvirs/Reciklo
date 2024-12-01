package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.TypeProductDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.models.TypeProductDTO
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body

class TypeProductDataSourceRemote() : TypeProductDataSourceRemoteInterface {
    override suspend fun getTypeProduct(token:String): StatusResult<TypeProductDTO> {
        val response =
            BaseClient.baseClient.get(BaseClient.BACKEND_URL+"/private/product/types", "Fallo al pedir tipos de producto", token)
        try {
            response.httpResponse.let {
                if (it != null) {
                    return StatusResult.Success(it.body())
                }
                return StatusResult.Error(response.errorMessage)
            }
        } catch (e: Exception) {
            return StatusResult.Error(e.message.toString())
        }
    }

}