package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.StatusResult
import com.reciklo.db.ModelCarDB

interface ModelCarDataSourceLocalInterface {
    fun insert(modelCar: String)

    fun deleteModel()

    suspend fun getAll(): StatusResult<List<ModelCarDB>>
}