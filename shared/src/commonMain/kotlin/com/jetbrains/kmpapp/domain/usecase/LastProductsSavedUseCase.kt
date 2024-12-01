package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.data.repository.LastProductsSavedRepository
import com.jetbrains.kmpapp.domain.interfaces.LastProductsSavedRepositoryInterface

class LastProductsSavedUseCase(private val repository: LastProductsSavedRepositoryInterface) {
     suspend fun invoke(token:String) = repository.getLastProductsSaved(token)
}