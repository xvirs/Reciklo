package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.reciklo.db.TypeProductDB

interface ListTypeDataSourceLocal {
    suspend fun insertType(type: String)
    suspend fun getAllTypes(): StatusResult<List<TypeProductDB>>
    suspend fun deleteType()
}