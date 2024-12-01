package com.jetbrains.kmpapp.data.datasource.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jetbrains.kmpapp.data.interfaces.ListCategoryDataSourceLocal
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.reciklo.db.CategoryProductDB
import com.reciklo.db.RecikloDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.firstOrNull

class CategoryProductDataSourceLocal(db: RecikloDataBase): ListCategoryDataSourceLocal {
    private val queriesCategory = db.categoryProductDBQueries

    override suspend fun insertCategory(category: String) {
        queriesCategory.insert(category)
    }

    override suspend fun getAllCategory(): StatusResult<List<CategoryProductDB>> {
        val result = queriesCategory.getAll()
        try {
            result.let {
                if (it!=null){
                    val response = it.asFlow().mapToList(Dispatchers.IO).firstOrNull()
                    return StatusResult.Success(value = response!!)
                }
                return StatusResult.Error(message = "falla al buscar tipos de productos localmente")
            }
        }catch (e:Exception){
            return StatusResult.Error(message = e.message!!)
        }
    }

    override suspend fun deleteCategory() {
        queriesCategory.delete()
    }

}