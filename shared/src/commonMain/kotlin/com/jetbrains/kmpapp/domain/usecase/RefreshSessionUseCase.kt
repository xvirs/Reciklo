package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.RefreshSessionInterface

class RefreshSessionUseCase(private val repository: RefreshSessionInterface) {
    suspend fun invoke(token:String) = repository.refreshToken(token)

}