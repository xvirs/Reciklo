package com.jetbrains.kmpapp.domain.usecase

import com.jetbrains.kmpapp.domain.interfaces.DataVehicleCompleteRepositoryInterface
import com.jetbrains.kmpapp.domain.models.Image
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.DataVehicleComplete

class DataVehicleCompleteUseCase(private val repository : DataVehicleCompleteRepositoryInterface ) {
    suspend fun invoke( image: Image, token : String ) : StatusResult<DataVehicleComplete> = repository.getVehicleComplete(image, token )
}