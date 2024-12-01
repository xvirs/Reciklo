package com.jetbrains.kmpapp.data.datasource.remote

import com.jetbrains.kmpapp.data.interfaces.CategoryDataSourceInterface
import com.jetbrains.kmpapp.data.models.CategoryDto
import com.jetbrains.kmpapp.data.network.BaseClient
import com.jetbrains.kmpapp.domain.models.StatusResult
import io.ktor.client.call.body

class CategoryDataSourceRemote() : CategoryDataSourceInterface {
    override suspend fun getCategory(): StatusResult<CategoryDto> {
        val response = BaseClient.baseClient.get(BaseClient.BACKEND_URL + "/category", "Falla al pedir categoria", "")
        try {
            response.httpResponse.let {
                if (it != null){
                    return StatusResult.Success(value = it.body())
                }
            }
            return StatusResult.Error(message = "Error al obtener categoria")
        } catch (e: Exception) {
            return StatusResult.Error(e.message.toString())
        }
    }

}