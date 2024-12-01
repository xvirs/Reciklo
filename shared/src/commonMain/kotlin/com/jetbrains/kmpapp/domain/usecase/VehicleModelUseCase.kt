package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.VehicleModelRepositoryInterface

class VehicleModelUseCase(private val repository: VehicleModelRepositoryInterface) {
    suspend fun invoke(token:String, model:Int) = repository.getVehicleModel(token, model)
}