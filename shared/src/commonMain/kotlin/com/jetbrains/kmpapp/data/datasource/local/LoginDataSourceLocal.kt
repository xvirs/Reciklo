package com.jetbrains.kmpapp.data.datasource.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jetbrains.kmpapp.data.interfaces.LoginDataSourceLocalInterface
import com.jetbrains.kmpapp.data.models.UserSerializable
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.reciklo.db.RecikloDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.firstOrNull

class LoginDataSourceLocal(db: RecikloDataBase) : LoginDataSourceLocalInterface {
    private val sessionDBQueries = db.sessionDBQueries
    override fun insert(user: String) {
        sessionDBQueries.insert(user)
    }

    override fun delete() {
        sessionDBQueries.delete()
    }

    override suspend fun getAll(): StatusResult<List<UserSerializable>?> {
        val response = sessionDBQueries.getAll()
        try {
            response.let {
                val status = response.asFlow().mapToList(Dispatchers.IO).firstOrNull()
                return StatusResult.Success(status?.map {
                    UserSerializable(it.session!!)
                })
            }
        } catch (e: Exception) {
            return StatusResult.Error(e.message!!)
        }
    }
}