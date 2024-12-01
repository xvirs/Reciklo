package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.interfaces.ModelCarDataSourceLocalInterface
import com.jetbrains.kmpapp.data.interfaces.ModelCarDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.mappers.toModel
import com.jetbrains.kmpapp.data.mappers.toModelSerializable
import com.jetbrains.kmpapp.data.models.ListManufacturerSerializable
import com.jetbrains.kmpapp.data.models.ModelCarListJson
import com.jetbrains.kmpapp.domain.interfaces.ModelCarRepositoryInterface
import com.jetbrains.kmpapp.domain.models.ListManufacturer
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.User
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ModelCarRepository(
    private val dataSourceRemote: ModelCarDataSourceRemoteInterface,
    private val dataSourceLocal: ModelCarDataSourceLocalInterface
) : ModelCarRepositoryInterface {
    override suspend fun getModelCar(statusSession: SessionManager<User>): StatusResult<ListManufacturer> {
        return when(statusSession){
            is SessionManager.Offline -> getLocal()
            is SessionManager.Online -> getRemote()
        }
    }

    override suspend fun getModelCarWithQuery(query: String): StatusResult<ListManufacturer> {
        return when(val response = dataSourceRemote.getModelCar((query))){
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value.toModel())
        }
    }

    suspend fun getLocal():StatusResult<ListManufacturer>{
        return when (val result = dataSourceLocal.getAll()) {
            is StatusResult.Error -> {
                StatusResult.Error("Error de lectura de base de datos")
            }

            is StatusResult.Success -> {
                val listJson = result.value.map {
                    ModelCarListJson(
                        it.model!!
                    )
                }
                if (listJson.isNotEmpty()) {
                    val json =
                        Json.decodeFromString<ListManufacturerSerializable>(listJson[0].listModelCar)
                    return StatusResult.Success(json.toModel())
                }
                StatusResult.Error("Modelos no recuperados correctamente")
            }
        }
    }
    suspend fun getRemote() : StatusResult<ListManufacturer> {
        return when(val result = getLocal()){
            is StatusResult.Error -> {
                when ( val response = dataSourceRemote.getModelCar("") ) {
                    is StatusResult.Error -> StatusResult.Error(response.message)
                    is StatusResult.Success -> {
                        val dataSerializable = response.value.toModelSerializable()
                        dataSourceLocal.deleteModel().let {
                            dataSourceLocal.insert(Json.encodeToString(dataSerializable))
                        }
                        StatusResult.Success(response.value.toModel())
                    }
                }
            }
            is StatusResult.Success -> {
                when(val response = dataSourceRemote.getModelCar((""))){
                    is StatusResult.Error -> StatusResult.Error(response.message)
                    is StatusResult.Success -> StatusResult.Success(result.value)
                }
            }
        }
    }
}