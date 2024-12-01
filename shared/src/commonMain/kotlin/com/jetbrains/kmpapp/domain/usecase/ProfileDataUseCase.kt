package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.ProfileDataRepositoryInterface


class ProfileDataUseCase(private val repository: ProfileDataRepositoryInterface) {
    suspend fun invoke(token:String) = repository.getProfile(token)

}