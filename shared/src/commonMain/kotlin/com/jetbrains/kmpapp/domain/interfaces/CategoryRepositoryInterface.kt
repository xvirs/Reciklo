package com.jetbrains.kmpapp.domain.interfaces

import com.jetbrains.kmpapp.domain.models.ListCategory
import com.jetbrains.kmpapp.domain.models.StatusResult

interface CategoryRepositoryInterface {
    suspend fun getCategory(): StatusResult<ListCategory>
}