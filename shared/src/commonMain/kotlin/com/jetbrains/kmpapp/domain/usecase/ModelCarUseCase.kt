package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.ModelCarRepositoryInterface
import com.jetbrains.kmpapp.domain.models.ListManufacturer
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User

class ModelCarUseCase(private val repository: ModelCarRepositoryInterface) {

    suspend fun invoke(statusSession: SessionManager<User>): StatusResult<ListManufacturer> {
        return when (val response = repository.getModelCar(statusSession)) {
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> {
                StatusResult.Success(response.value)
            }
        }
    }

    suspend fun getForQuery(query: String) = repository.getModelCarWithQuery(query)

}