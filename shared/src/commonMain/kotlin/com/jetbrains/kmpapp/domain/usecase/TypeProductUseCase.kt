package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.TypeProductRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.TypeProduct


class TypeProductUseCase(private val repository: TypeProductRepositoryInterface) {

    suspend fun invoke(token:String): StatusResult<TypeProduct> {
        return when (val response = repository.getTypeProduct(token)) {
            is StatusResult.Error -> StatusResult.Error(message = response.message)
            is StatusResult.Success -> StatusResult.Success(value = response.value)
        }
    }
}