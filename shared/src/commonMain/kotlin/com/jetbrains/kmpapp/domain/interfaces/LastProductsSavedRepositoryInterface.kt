package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.LatestProductData
import com.jetbrains.kmpapp.domain.models.StatusResult

interface LastProductsSavedRepositoryInterface {
    suspend fun getLastProductsSaved(token:String): StatusResult<LatestProductData>
}