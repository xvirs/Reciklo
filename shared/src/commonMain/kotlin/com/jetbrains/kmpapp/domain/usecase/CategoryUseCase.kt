package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.CategoryRepositoryInterface
import com.jetbrains.kmpapp.domain.models.ListCategory
import com.jetbrains.kmpapp.domain.models.StatusResult

class CategoryUseCase(private val repository: CategoryRepositoryInterface) {

    suspend fun invoke(): StatusResult<ListCategory> {
        return when (val response = repository.getCategory()) {
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
    }
}