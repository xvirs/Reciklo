package com.jetbrains.kmpapp.data.datasource.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jetbrains.kmpapp.data.interfaces.ListTypeDataSourceLocal
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.reciklo.db.RecikloDataBase
import com.reciklo.db.TypeProductDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.firstOrNull

class TypeProductDataSourceLocal(db : RecikloDataBase) : ListTypeDataSourceLocal {

    private val queriesType = db.typeProductDBQueries

    override suspend fun insertType(type: String) {
        queriesType.insert(type)
    }

    override suspend fun getAllTypes(): StatusResult<List<TypeProductDB>> {
        val result = queriesType.getAll()
        try {
           result.let {
               if (it!=null){
                   val response = it.asFlow().mapToList(Dispatchers.IO).firstOrNull()
                      return StatusResult.Success(value = response!! )
               }
               return StatusResult.Error(message = "falla al buscar tipos de productos localmente")
           }
       }catch (e:Exception){
           return StatusResult.Error(message = e.message!!)
       }
    }
    override suspend fun deleteType() {
        queriesType.delete()
    }
}