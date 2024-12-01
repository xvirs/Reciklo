package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.datasource.remote.LatestProductSavedDataSource
import com.jetbrains.kmpapp.data.interfaces.LatestProductSavedDataSourceInterface
import com.jetbrains.kmpapp.data.mappers.toModel
import com.jetbrains.kmpapp.domain.interfaces.LastProductsSavedRepositoryInterface
import com.jetbrains.kmpapp.domain.models.LatestProductData
import com.jetbrains.kmpapp.domain.models.StatusResult

class LastProductsSavedRepository(private val dataSource: LatestProductSavedDataSourceInterface): LastProductsSavedRepositoryInterface {
    override suspend fun getLastProductsSaved(token:String): StatusResult<LatestProductData> {
        return when(val response = dataSource.getLatestProductSaved(token = token)){
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> {
                StatusResult.Success(response.value.toModel())
            }
        }
    }
}