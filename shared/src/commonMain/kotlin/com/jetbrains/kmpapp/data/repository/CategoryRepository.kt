package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.datasource.local.CategoryProductDataSourceLocal
import com.jetbrains.kmpapp.data.interfaces.CategoryDataSourceInterface
import com.jetbrains.kmpapp.data.mappers.toModel
import com.jetbrains.kmpapp.data.mappers.toModelSerializable
import com.jetbrains.kmpapp.data.models.JsonListCategoryDTO
import com.jetbrains.kmpapp.data.models.ListCategorySerializable
import com.jetbrains.kmpapp.domain.interfaces.CategoryRepositoryInterface
import com.jetbrains.kmpapp.domain.models.ListCategory
import com.jetbrains.kmpapp.domain.models.StatusResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CategoryRepository(
    private val dataSource: CategoryDataSourceInterface,
    private val localDataSource: CategoryProductDataSourceLocal
) : CategoryRepositoryInterface {
    override suspend fun getCategory(): StatusResult<ListCategory> {
        return when (val response = dataSource.getCategory()) {
            is StatusResult.Error -> {
                when (val result = localDataSource.getAllCategory()) {
                    is StatusResult.Error -> StatusResult.Error("Error al obtener la categoria localmente")
                    is StatusResult.Success -> {
                        val jsonDTO = result.value.map {
                            JsonListCategoryDTO(it.category!!)
                        }
                        if (jsonDTO.isNotEmpty()){
                            val json =
                                Json.decodeFromString<ListCategorySerializable>(jsonDTO[0].listCategory)
                           return StatusResult.Success(json.toModel())
                        }
                        StatusResult.Error("datos aun no almacenados")

                    }
                }
            }

            is StatusResult.Success -> {
                val dataSerializable: ListCategorySerializable = response.value.toModelSerializable()
                val json = Json.encodeToString(dataSerializable)
                localDataSource.deleteCategory().let {
                    localDataSource.insertCategory(json)
                }
                StatusResult.Success(response.value.toModel())
            }
        }
    }
}