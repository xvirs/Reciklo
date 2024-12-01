package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.VehicleVersionRepositoryInterface

class VehicleVersionUseCase(private val repository : VehicleVersionRepositoryInterface) {

    suspend fun invoke(token:String, model:Int) = repository.getVersion(token, model)
}