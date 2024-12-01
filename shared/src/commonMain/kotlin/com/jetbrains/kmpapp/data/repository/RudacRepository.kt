package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.data.interfaces.RudacDataSourceRemoteInterface
import com.jetbrains.kmpapp.domain.interfaces.RudacRepositoryInterface
import com.jetbrains.kmpapp.domain.models.Rudac
import com.jetbrains.kmpapp.domain.models.StatusResult


class RudacRepository (private val dataSource : RudacDataSourceRemoteInterface) :
    RudacRepositoryInterface {

    override suspend fun getRudac(codRudac : String ) : StatusResult<Rudac> {
        return when(val response = dataSource.getRudac(codRudac)){
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> StatusResult.Success(response.value)
        }
    }
}