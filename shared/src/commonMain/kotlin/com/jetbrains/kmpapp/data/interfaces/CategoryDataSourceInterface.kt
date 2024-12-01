package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.data.models.CategoryDto

interface CategoryDataSourceInterface {
    suspend fun getCategory(): StatusResult<CategoryDto>
}