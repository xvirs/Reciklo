package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.data.models.TypeProductDTO

interface TypeProductDataSourceRemoteInterface {
    suspend fun getTypeProduct(token:String): StatusResult<TypeProductDTO>

}