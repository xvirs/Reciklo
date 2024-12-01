package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.data.models.LatestProductDataDTO
import com.jetbrains.kmpapp.domain.models.StatusResult

interface LatestProductSavedDataSourceInterface {
    suspend fun getLatestProductSaved(token:String): StatusResult<LatestProductDataDTO>
}