package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.TypeProduct

interface TypeProductRepositoryInterface {
    suspend fun getTypeProduct(token:String): StatusResult<TypeProduct>
}