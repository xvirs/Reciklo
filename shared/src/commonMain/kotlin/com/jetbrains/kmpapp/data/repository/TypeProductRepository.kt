package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.datasource.local.TypeProductDataSourceLocal
import com.jetbrains.kmpapp.data.interfaces.TypeProductDataSourceRemoteInterface
import com.jetbrains.kmpapp.data.mappers.toModel
import com.jetbrains.kmpapp.data.mappers.toModelSerializable
import com.jetbrains.kmpapp.data.mappers.toTypeProduct
import com.jetbrains.kmpapp.data.models.JsonListTypeDTO
import com.jetbrains.kmpapp.data.models.TypeProductToSerializable
import com.jetbrains.kmpapp.domain.interfaces.TypeProductRepositoryInterface
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.models.TypeProduct
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class TypeProductRepository(
    private val dataSourceRemote: TypeProductDataSourceRemoteInterface,
    private val dataSourceLocal: TypeProductDataSourceLocal
) : TypeProductRepositoryInterface {
    override suspend fun getTypeProduct(token: String): StatusResult<TypeProduct> {

        return when (val response = dataSourceRemote.getTypeProduct(token)) {
            is StatusResult.Error -> {
                when (val result = dataSourceLocal.getAllTypes()) {
                    is StatusResult.Error -> StatusResult.Error("fallo la busqueda local")
                    is StatusResult.Success -> {
                        val aux = result.value.map {
                            JsonListTypeDTO(it.type!!)
                        }
                        if (aux.isNotEmpty()){
                            val json = Json.decodeFromString<TypeProductToSerializable>(aux[0].listTypes)
                            return StatusResult.Success(json.toTypeProduct())
                        }
                        StatusResult.Error("datos no almacenados")

                    }
                }
            }

            is StatusResult.Success -> {
                val dataSerializable : TypeProductToSerializable = response.value.toModelSerializable()
                val json = Json.encodeToString(dataSerializable)
                dataSourceLocal.deleteType().let {
                    dataSourceLocal.insertType(json)
                }
                StatusResult.Success(response.value.toModel())
            }
        }
    }

}