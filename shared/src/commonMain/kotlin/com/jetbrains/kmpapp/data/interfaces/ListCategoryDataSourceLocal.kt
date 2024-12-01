package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.reciklo.db.CategoryProductDB

interface ListCategoryDataSourceLocal {
    suspend fun insertCategory(category: String)
    suspend fun getAllCategory(): StatusResult<List<CategoryProductDB>>
    suspend fun deleteCategory()
}