package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.RudacRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult

class RudacUseCase(private val repository : RudacRepositoryInterface) {

    suspend operator fun invoke(codRudac : String) =
        when(val response = repository.getRudac(codRudac)){
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
}