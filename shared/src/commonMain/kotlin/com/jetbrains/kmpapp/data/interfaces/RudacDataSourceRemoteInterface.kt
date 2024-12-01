package com.jetbrains.kmpapp.data.interfaces

import com.jetbrains.kmpapp.domain.models.Rudac
import com.jetbrains.kmpapp.domain.models.StatusResult

interface RudacDataSourceRemoteInterface {
    suspend fun getRudac(codRudac : String) : StatusResult<Rudac>
}