package com.jetbrains.kmpapp.data.datasource.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jetbrains.kmpapp.data.interfaces.ModelCarDataSourceLocalInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.reciklo.db.ModelCarDB
import com.reciklo.db.RecikloDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.firstOrNull

class ModelCarDataSourceLocal(private val db: RecikloDataBase) : ModelCarDataSourceLocalInterface {

    val queries = db.modelCarDBQueries
    override fun insert(modelCar: String) {
        queries.insert(modelCar)
    }

    override fun deleteModel() {
        queries.delete()
    }

    override suspend fun getAll(): StatusResult<List<ModelCarDB>> {
        val response = queries.getAll()
        try {
            response.let {
                val response = it.asFlow().mapToList(Dispatchers.IO).firstOrNull()
                return StatusResult.Success(response!!)
            }
        } catch (e: Exception) {
            return StatusResult.Error(e.message!!)
        }
    }
}